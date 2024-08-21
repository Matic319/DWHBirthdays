package com.maticz.BirthdaysDWH.service.invites.impl;

import com.maticz.BirthdaysDWH.config.ThymeleafConfiguration;
import com.maticz.BirthdaysDWH.repository.BirthdayPricesRepository;
import com.maticz.BirthdaysDWH.repository.BirthdaysRepository;
import com.maticz.BirthdaysDWH.service.impl.MailSendingServiceImpl;
import com.maticz.BirthdaysDWH.service.impl.PdfTextInsertionServiceImpl;
import com.maticz.BirthdaysDWH.service.invites.InviteTextInsertionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class InviteTextInsertionServiceImpl implements InviteTextInsertionService {

    @Autowired
    PdfTextInsertionServiceImpl pdfTextInsertionService;

    @Autowired
    BirthdayPricesRepository birthdayPricesRepository;

    @Autowired
    MailSendingServiceImpl mailSendingService;


    @Autowired
    ThymeleafConfiguration thymeleafConfiguration;

    @Autowired
    BirthdaysRepository birthdaysRepository;


    DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    DateTimeFormatter formatterInviteDate = DateTimeFormatter.ofPattern("d.M.yyyy");
    DateTimeFormatter formatterInviteTime = DateTimeFormatter.ofPattern("HH:mm");



    @Override
    public byte[] createPdfInMemoryInvite(String age, String date, String startTime, Integer idLocation, String endTime,
                                          String phone, String childName, Integer idProgType) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(setTemplate(idProgType));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDDocument document = Loader.loadPDF(inputStream.readAllBytes());
            PDPage page = document.getPage(0);
            PDRectangle pageSize = page.getMediaBox();
            float pageHeightInPoints = pageSize.getHeight();

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {

                insertTextIntoInviteTemplate(age,date,startTime,idLocation,endTime,phone,childName, idProgType, contentStream,pageHeightInPoints,document);
            }

            document.save(outputStream);
            document.close();

            return outputStream.toByteArray();
        }
    }

    @Override
    public void insertTextIntoInviteTemplate(String age, String date, String startTime, Integer idLocation, String endTime,
                                             String phone, String childName, Integer idProgType , PDPageContentStream contentStream, float pageHeightInPoints, PDDocument document) throws IOException {

        contentStream.setNonStrokingColor(1f, 1f, 1f);


        pdfTextInsertionService.insertText(
                contentStream, pdfTextInsertionService.setAttractionText(idProgType), 1.8f, 3.1f, pageHeightInPoints, 34,document);
        pdfTextInsertionService.insertText(
                contentStream, age +".", 2.65f, 1.85f, pageHeightInPoints, 52,document);
        pdfTextInsertionService.insertText(
                contentStream, date, 2.62f, 4.2f, pageHeightInPoints, 18,document);
        pdfTextInsertionService.insertText(
                contentStream, startTime, 2.62f, 4.99f, pageHeightInPoints, 18,document);
        pdfTextInsertionService.insertText(
                contentStream, pdfTextInsertionService.locationName(idLocation), 2.62f, 5.75f, pageHeightInPoints, 18,document);
        pdfTextInsertionService.insertText(
                contentStream, pdfTextInsertionService.locationAddress(idLocation), 2.62f, 6.55f, pageHeightInPoints, 18,document);
        pdfTextInsertionService.insertText(
                contentStream, endTime, 2.62f, 7.3f, pageHeightInPoints, 18,document);
        pdfTextInsertionService.insertText(
                contentStream, pdfTextInsertionService.convertPhoneNumber(phone), 3.48f, 8.38f, pageHeightInPoints, 18,document);
        pdfTextInsertionService.insertText(
                contentStream, childName, 5.9f, 11.1f, pageHeightInPoints, 30,document);


    }


    public void sendTestEmail(String toEmail, Integer idLocation) throws MessagingException {

        MimeMessage message = mailSendingService.setJavaMailSender(100).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);


        Context textForEmail = setTextForEmail("20.8","17:30","Skakalna zabava" ,"8","imeStarsa",
                "38631331180","20:15", "komentarji abc","8",
                "14",38,17,true, "MEGA");

        String thymleafString = thymeleafConfiguration.templateEngine().process("mail_template",textForEmail);

        helper.setTo("matic.zigon@woop.fun");
        helper.setSubject("Template test");
        helper.setText(thymleafString,true);

        mailSendingService.setJavaMailSender(100).send(message);
    }



    @Override
    public Context setTextForEmail(String dateFrom, String startTime, String progType,
                                   String participantCount, String parentName, String phone,
                                   String endTime, String parentComments,
                                   String minAge, String maxAge, Integer idProgType, Integer idPartyType, Boolean animatorRequired, String partyType) {
        Context context = new Context();

        int participantCountInt = Integer.parseInt(participantCount);
        Float pricePerPerson = setPricePerPerson(idProgType, idPartyType);

        context.setVariable("dateFrom", dateFrom);
        context.setVariable("startTime", startTime);
        context.setVariable("progType",progType);
        context.setVariable("partyType",partyType);
        context.setVariable("price",pricePerPerson);
        if(idProgType == 44 && participantCountInt < 6 )  { //ER zabava je za 6 ljudi min !
            context.setVariable(
                    "priceForAll", pricePerPerson* participantCountInt);
            context.setVariable("participantCount", participantCount);
        }if(participantCountInt < 10) {
            context.setVariable(
                    "priceForAll", pricePerPerson * 10);
            context.setVariable("participantCount", participantCount);
        }

        else {
            context.setVariable(
                    "priceForAll", setPricePerPerson(idProgType, idPartyType) * participantCountInt);
            context.setVariable("participantCount", participantCount);
        }


        if(!Objects.equals(minAge, maxAge)) {
            context.setVariable("age", "od " + minAge + " do " + maxAge + " let");
        } else {
            context.setVariable("age", minAge + " let");
        }

        context.setVariable("parentName",parentName);
        context.setVariable("phone",pdfTextInsertionService.convertPhoneNumber(phone));
        context.setVariable("location", setLocation(idProgType));
        context.setVariable("endTime", endTime);
        context.setVariable("comments", parentComments);


        if(animatorRequired){
            context.setVariable("requiredAnimator","je potreben zaradi nehomogenosti skupine (doplačilo 50 €). V primeru, da dodatnega animatorja ne želite, je obvezno spremstvo staršev oziroma druge odrasle osebe.");
        }else {
            context.setVariable("requiredAnimator","ni potreben");
        }
        return context;
    }



    private String setTemplate(Integer idProgType){
        return switch (idProgType){
            case 38 -> "/inviteTP.pdf";
            case 9 -> "/inviteFW.pdf";
            case 39 -> "/inviteKarting.pdf";
            case 41 -> "/inviteCubes.pdf";
            case 42 -> "/inviteER.pdf";
            case 43 -> "/inviteGG.pdf";
            case 44 -> "/inviteBowling.pdf";
            case 45 -> "/inviteLT.pdf";


            default -> throw new IllegalArgumentException("idProgType napačn");
        };
    }

    private float setPricePerPerson(Integer idProgType, Integer idPartyType){
        return birthdayPricesRepository.getPricePerPerson(idProgType,idPartyType);
    }

    private String setLocation(Integer idProgType) {
        return switch (idProgType) {
          case 38,9 -> "WOOP! Fun park, Leskoškova cesta 3, v 1. nadstropju pri Oraketi.";
            default -> "";
        };
    }
}
