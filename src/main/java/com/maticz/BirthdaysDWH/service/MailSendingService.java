package com.maticz.BirthdaysDWH.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface MailSendingService {

    void sendBirthdayInvitationsEmail(Integer idLocation);

    void sendBDayForm(Integer idLocation) throws MessagingException, IOException;
}
