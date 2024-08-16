package com.maticz.BirthdaysDWH.controller;

import com.maticz.BirthdaysDWH.service.impl.MailSendingServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/forms")
public class BirthdayFormsController {

    @Autowired
    MailSendingServiceImpl mailSendingService;

    @Scheduled(cron = "0 0 14 * * *")
    @GetMapping("/send")
    public ResponseEntity<String> sendBirthdayForm() throws MessagingException, IOException {
        mailSendingService.sendBDayForm(1,"info@woop.fun");
        mailSendingService.sendBDayForm(2,"infokarting@woop.fun");
        mailSendingService.sendBDayForm(3,"infoarena@woop.fun");
        mailSendingService.sendBDayForm(5,"inforudnik@woop.fun");
        mailSendingService.sendBDayForm(6,"infomb@woop.fun");

        return ResponseEntity.ok("ok");

    }

    @GetMapping("/sendTest")
    public ResponseEntity<String> test() throws MessagingException, IOException {
        mailSendingService.sendBDayForm(1,"matic.zigon@woop.fun");


        return ResponseEntity.ok("ok");

    }

}
