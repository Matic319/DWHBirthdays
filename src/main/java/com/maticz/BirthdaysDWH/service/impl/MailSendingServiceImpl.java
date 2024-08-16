package com.maticz.BirthdaysDWH.service.impl;

import com.maticz.BirthdaysDWH.config.ThymeleafConfiguration;
import com.maticz.BirthdaysDWH.repository.BirthdayInvitationsRepository;
import com.maticz.BirthdaysDWH.repository.BirthdaysRepository;
import com.maticz.BirthdaysDWH.service.MailSendingService;
import com.maticz.BirthdaysDWH.service.PDFTextInsertionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MailSendingServiceImpl implements MailSendingService {

    @Autowired
    BirthdayInvitationsRepository birthdayInvitationsRepository;

    @Autowired
    @Qualifier("maticEmail")
    private JavaMailSender mailSenderMatic;

    @Autowired
    @Qualifier("infoTP")
    private JavaMailSender mailSenderTP;

    @Autowired
    @Qualifier("infoKarting")
    private JavaMailSender mailSenderKarting;

    @Autowired
    @Qualifier("infoArena")
    private JavaMailSender mailSenderArena;

    @Autowired
    @Qualifier("infoRudnik")
    private JavaMailSender mailSenderRudnik;

    @Autowired
    @Qualifier("infoMB")
    private JavaMailSender mailSenderMB;


    @Autowired
    PDFTextInsertionService pdfTextInsertionService;

    @Autowired
    ThymeleafConfiguration thymeleafConfiguration;

    @Autowired
    BirthdaysRepository birthdaysRepository;


    //private final TemplateEngine templateEngine;

    DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    DateTimeFormatter formatterInviteDate = DateTimeFormatter.ofPattern("d.M.yyyy");
    DateTimeFormatter formatterInviteTime = DateTimeFormatter.ofPattern("HH:mm");

   /*public MailSendingServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }*/

    public void sendBirthdayInvitationsEmail(Integer idLocation) {  //nastav, da bo toEmail od strank !!
        List<Object[]> listOfClientsToSend = birthdayInvitationsRepository.getAllClientsThatHaveNotReceivedTheInvitation(idLocation);

        for (Object[] row : listOfClientsToSend) {
            LocalDateTime dateFrom = LocalDateTime.parse(row[0].toString(), formatterDB);
            String date = dateFrom.toLocalDate().format(formatterInviteDate);
            String startTime = dateFrom.toLocalTime().format(formatterInviteTime);
            String childName = row[2].toString();
            String age = row[3].toString();
            String phone = pdfTextInsertionService.convertPhoneNumber(row[4].toString());
            String food = row[18].toString();
            String desserts = row[19].toString();
            String minAge = row[20].toString();
            String maxAge = row[21].toString();
            String participantCount = row[22].toString();
            String comments = row[23].toString();
            String partyType = row[26].toString();
            String parentFirstName = row[27].toString();
            String parentLastName = row[28].toString();
            LocalDateTime dateTo = LocalDateTime.parse(row[29].toString(),formatterDB);
            String endTime = dateTo.toLocalTime().format(formatterInviteTime);

            String locationNameAndAddress = pdfTextInsertionService.locationName(idLocation) + ", " + pdfTextInsertionService.locationAddress(idLocation);


            try {
                byte[] jpgData = pdfTextInsertionService.createAndConvertPdfToJpg(age, date, startTime, phone, childName, idLocation);
                Context context = getContext(date, startTime,partyType,participantCount,age,
                        parentFirstName + " " + parentLastName,
                        phone, locationNameAndAddress, endTime,food,desserts,comments,
                        minAge, maxAge);
                sendEmail("matic.zigon@woop.fun", childName, jpgData,idLocation,context);
                birthdayInvitationsRepository.updateEmailSent(idLocation,dateFrom,"matic.zigon@woop.fun",childName);


            } catch (Exception e) {
                System.err.println("Failed to send invitation to " + "clientEmail" + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void sendBDayForm(Integer idLocation, String sendTo) throws MessagingException, IOException {
        List<Object[]> getFormDataList = birthdaysRepository.getBdayFormData(idLocation);

        if (getFormDataList.isEmpty()) {
            return;
        }

        MimeMessage message = mailSenderMatic.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(sendTo);
        helper.setSubject("RD obrazci " + LocalDate.now().plusDays(1).toString());
        helper.setText("lp");

        boolean hasAttachments = false;

        for (Object[] row : getFormDataList) {
            LocalDateTime dateTimeStart = LocalDateTime.parse(row[0].toString(), formatterDB);
            LocalDateTime dateTimeEnd = LocalDateTime.parse(row[13].toString(), formatterDB);
            String date = dateTimeStart.format(formatterInviteDate);
            String startTime = dateTimeStart.format(formatterInviteTime);
            String programName = row[1].toString();
            String subProgramName = row[2] != null ? row[2].toString() : null;
            String partyPlace = row[3] != null ? row[3].toString() : null;
            String childName = row[4].toString();
            String childSurname = row[5].toString();
            String age = row[6] != null ? row[6].toString() : null;
            String phone = row[7] != null ? row[7].toString() : null;
            String participantCount = row[8] != null ? row[8].toString() : null;
            String parentFirstname = row[9] != null ? row[9].toString() : null;
            String minAge = row[10] != null ? row[10].toString() : null;
            String maxAge = row[11] != null ? row[11].toString() : null;
            String endTime = dateTimeEnd.format(formatterInviteTime);
            String inviteComments = row[12] != null ? row[12].toString() : null;
            String animator = row[14] != null ? row[14].toString() : null;

            byte[] attachment = pdfTextInsertionService.createPdfInMemoryBDayForm(date, startTime, endTime,
                    programName, childName, childSurname, participantCount, age, phone, partyPlace,
                    minAge, maxAge, parentFirstname, inviteComments, animator, subProgramName);

            if (attachment != null && attachment.length > 0) {
                helper.addAttachment(childName + "_" + childSurname + ".pdf", new ByteArrayResource(attachment));
                hasAttachments = true;
            }

        }

        if (hasAttachments) {
            mailSenderMatic.send(message);
        }
    }

    private void sendEmail(String toEmail, String childName, byte[] attachment, Integer idLocation, Context context) throws MessagingException {

        MimeMessage message = setJavaMailSender(idLocation).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String thymleafString = thymeleafConfiguration.templateEngine().process("mail_template",context);

        helper.setTo(toEmail);
        helper.setSubject("Vabilo za " + childName);
        helper.setText(thymleafString,true);

        helper.addAttachment("vabilo_" + childName +".jpg", new ByteArrayDataSource(attachment, "image/jpeg"));

        setJavaMailSender(idLocation).send(message);
    }

    public void sendTestEmail(String toEmail, Integer idLocation) throws MessagingException {

        MimeMessage message = setJavaMailSender(idLocation).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);


        Context context = new Context();
        context.setVariable("dateFrom", "12.1.2024");

        String thymleafString = thymeleafConfiguration.templateEngine().process("mail_template",context);

        helper.setTo(toEmail);
        helper.setSubject("Template test");
        helper.setText(thymleafString,true);

        setJavaMailSender(idLocation).send(message);
    }

    private  Context getContext(String dateFrom, String startTime, String partyType,
                                String participantCount, String age, String parentFullName, String phone,
                                String location, String endTime, String food, String desserts, String comments,
                                String minAge, String maxAge) {
        Context context = new Context();

        context.setVariable("dateFrom", dateFrom);
        context.setVariable("startTime", startTime);
        context.setVariable("partyType",partyType);
        context.setVariable("price",setPriceForPartyType(partyType));
        context.setVariable("participantCount", participantCount);
        context.setVariable("age", age);
        context.setVariable("parentFullName",parentFullName);
        context.setVariable("phone",phone);
        context.setVariable("location", location);
        context.setVariable("endTime", endTime);
        context.setVariable("food",food);
        context.setVariable("desserts",desserts);
        context.setVariable("comments", comments);
        context.setVariable("minAge", minAge);
        context.setVariable("maxAge", maxAge);
        return context;
    }


    private JavaMailSender setJavaMailSender(Integer idLocation) {
        JavaMailSender mailSender = null;
        switch(idLocation) {
            case 1 ->  mailSender = mailSenderTP;
            case 2 -> mailSender = mailSenderKarting;
            case 3 -> mailSender = mailSenderArena;
            case 5 -> mailSender = mailSenderRudnik;
            case 6 -> mailSender = mailSenderMB;
            case 100 -> mailSender = mailSenderMatic;
        }
        return mailSender;
    }

    private String setPriceForPartyType(String partyType) {


        return switch(partyType){
            case "Super fun zabava" -> "27.50";
            case "Skakalna zabava", "Plezalna zabava" -> "21.50";
            case "Mega fun zabava" -> "31.50";
            case "Lov na zaklad TEMATSKI" -> "1";
            case "Lov na zaklad ČRKE" -> "2";
            case "Lov na zaklad SIMBOLI" -> "32";
            default -> "5";
        };
    }


}
