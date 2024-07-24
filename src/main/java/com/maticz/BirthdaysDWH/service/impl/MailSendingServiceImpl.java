package com.maticz.BirthdaysDWH.service.impl;

import com.maticz.BirthdaysDWH.config.ThymeleafConfiguration;
import com.maticz.BirthdaysDWH.repository.BirthdayInvitationsRepository;
import com.maticz.BirthdaysDWH.service.MailSendingService;
import com.maticz.BirthdaysDWH.service.PDFTextInsertionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            LocalDateTime dateTime = LocalDateTime.parse(row[0].toString(), formatterDB);
            String date = dateTime.toLocalDate().format(formatterInviteDate);
            String startTime = dateTime.toLocalTime().format(formatterInviteTime);
            String childName = row[2].toString();
            String age = row[3].toString();
            String phone = row[4].toString();
            String food = row[18].toString();
            String desserts = row[19].toString();
            String minAge = row[20].toString();
            String maxAge = row[21].toString();
            String participantCount = row[22].toString();
            String partyType = row[26].toString();
            String parentFirstName = row[27].toString();
            String parentLastName = row[28].toString();

            try {
                byte[] jpgData = pdfTextInsertionService.createAndConvertPdfToJpg(age, date, startTime, phone, childName, idLocation);
                Context context = getContext(date, startTime)
                sendEmail("matic.zigon@woop.fun", childName, jpgData,idLocation);
                birthdayInvitationsRepository.updateEmailSent(idLocation,dateTime,"matic.zigon@woop.fun",childName);


            } catch (Exception e) {
                System.err.println("Failed to send invitation to " + "clientEmail" + ": " + e.getMessage());
            }
        }
    }

    private void sendEmail(String toEmail, String childName, byte[] attachment, Integer idLocation, Context context) throws MessagingException {

        MimeMessage message = setJavaMailSender(idLocation).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String thymleafString = thymeleafConfiguration.templateEngine().process("mail_template",context);

        helper.setTo(toEmail);
        helper.setSubject("Vabilo za " + childName);
        helper.setText("vabilo");

        helper.addAttachment("vabilo_" + childName +".jpg", new ByteArrayDataSource(attachment, "image/jpeg"));

        setJavaMailSender(idLocation).send(message);
    }

    public void sendTestEmail(String toEmail, Integer idLocation) throws MessagingException {

        MimeMessage message = setJavaMailSender(idLocation).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);


        Context context = getContext();


        String thymleafString = thymeleafConfiguration.templateEngine().process("mail_template",context);

        helper.setTo(toEmail);
        helper.setSubject("Template test");
        helper.setText(thymleafString,true);

        setJavaMailSender(idLocation).send(message);
    }

    private  Context getContext(String dateFrom, String startTime, String partyType,
                                String price, String participantCount, Integer age, String parentFullName, String phone,
                                String location, LocalTime endTime, String food, String desserts, String comments  ) {
        Context context = new Context();

        context.setVariable("dateFrom", dateFrom);
        context.setVariable("startTime", startTime);
        context.setVariable("partyType",partyType);
        context.setVariable("price",price);
        context.setVariable("participantCount", participantCount);
        context.setVariable("age", age);
        context.setVariable("parentFullName",parentFullName);
        context.setVariable("phone",phone);
        context.setVariable("location", location);
        context.setVariable("endTime", endTime);
        context.setVariable("food",food);
        context.setVariable("desserts",desserts);
        context.setVariable("comments",comments);
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


}
