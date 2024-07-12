package com.maticz.BirthdaysDWH.service.impl;

import com.maticz.BirthdaysDWH.repository.BirthdayInvitationsRepository;
import com.maticz.BirthdaysDWH.service.MailSendingService;
import com.maticz.BirthdaysDWH.service.PDFTextInsertionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MailSendingServiceImpl implements MailSendingService {

    @Autowired
    BirthdayInvitationsRepository birthdayInvitationsRepository;

    @Autowired
    private JavaMailSender mailSender;


    @Autowired
    PDFTextInsertionService pdfTextInsertionService;

    DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    DateTimeFormatter formatterInviteDate = DateTimeFormatter.ofPattern("d.M.yyyy");
    DateTimeFormatter formatterInviteTime = DateTimeFormatter.ofPattern("HH:mm");

    public void sendBirthdayInvitationsEmail(Integer idLocation) {
        List<Object[]> listOfClientsToSend = birthdayInvitationsRepository.getAllClientsThatHaveNotReceivedTheInvitation(idLocation);

        for (Object[] row : listOfClientsToSend) {
            LocalDateTime dateTime = LocalDateTime.parse(row[0].toString(), formatterDB);
            String date = dateTime.toLocalDate().format(formatterInviteDate);
            String time = dateTime.toLocalTime().format(formatterInviteTime);
            String childName = row[2].toString();
            String age = row[3].toString();
            String phone = row[4].toString();

            try {
                byte[] jpgData = pdfTextInsertionService.createAndConvertPdfToJpg(age, date, time, phone, childName, idLocation);
                sendEmail("matic.zigon@woop.fun", childName, jpgData);
                birthdayInvitationsRepository.updateEmailSent(1,dateTime,"matic.zigon@woop.fun",childName);


            } catch (Exception e) {
                System.err.println("Failed to send invitation to " + "clientEmail" + ": " + e.getMessage());
            }
        }
    }

    private void sendEmail(String toEmail, String childName, byte[] attachment) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("matic.zigon@woop.fun");
        helper.setTo(toEmail);
        helper.setSubject("Vabilo za " + childName);
        helper.setText("vabilo");

        helper.addAttachment("vabilo_" + childName +".jpg", new ByteArrayDataSource(attachment, "image/jpeg"));

        mailSender.send(message);
    }

}
