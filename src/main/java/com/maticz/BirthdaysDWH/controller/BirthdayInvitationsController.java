package com.maticz.BirthdaysDWH.controller;

import com.maticz.BirthdaysDWH.service.impl.BirthdayInvitationsServiceImpl;
import com.maticz.BirthdaysDWH.service.impl.MailSendingServiceImpl;
import com.maticz.BirthdaysDWH.service.invites.impl.InviteTextInsertionServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/invites")
public class BirthdayInvitationsController {

    @Autowired
    BirthdayInvitationsServiceImpl birthdayInvitationsService;

    @Autowired
    MailSendingServiceImpl mailSendingService;

    @Autowired
    InviteTextInsertionServiceImpl inviteTextInsertionService;

    @GetMapping("/test")
    public ResponseEntity<String> saveDAta() throws IOException {
        birthdayInvitationsService.mapAndSaveEmailInvitationData("1mbEZtS329eu7miy42dWSIvECvjHeNosIOALv-S236a8", "Trampolin park", 1);
        return ResponseEntity.ok("ok");
    }


    @GetMapping("/testInvite")
    public ResponseEntity<String> testInvite() throws MessagingException, IOException {
        inviteTextInsertionService.sendTestEmail("matic.zigon@woop.fun",100);
    return ResponseEntity.ok("ok");

    }

}