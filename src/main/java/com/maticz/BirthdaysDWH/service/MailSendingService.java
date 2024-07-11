package com.maticz.BirthdaysDWH.service;

import org.springframework.stereotype.Service;

@Service
public interface MailSendingService {

    void sendBirthdayInvitationsEmail(Integer idLocation);
}
