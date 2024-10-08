package com.maticz.BirthdaysDWH.service;

import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface MailSendingService {

    void sendBirthdayInvitationsEmail(Integer idLocation);

    void sendBDayForm(Integer idLocation, String sendTo) throws MessagingException, IOException;

    JavaMailSender setJavaMailSender(Integer idLocation);
}
