package com.maticz.BirthdaysDWH.controller;

import com.maticz.BirthdaysDWH.service.GoogleCalendarInviteService;
import com.maticz.BirthdaysDWH.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/pictures")
public class PicturesController {

    @Autowired
    BirthdayServiceImpl birthdayServiceImpl;

    @Autowired
    BirthdayCancelationServiceImpl birthdayCancelationServiceImpl;

    @Autowired
    PictureLinksServiceImpl pictureLinksService;

    @Autowired
    ACServiceImpl acService;

    @Autowired
    PdfTextInsertionServiceImpl pdfTextInsertionService;

    @Autowired
    BirthdayInvitationsServiceImpl birthdayInvitationsService;

    @Autowired
    MailSendingServiceImpl mailSendingService;

    @Autowired
    GoogleCalendarInviteServiceImpl googleCalendarInviteService;

    @Scheduled(cron = "0 30 */2 * * *")
    @GetMapping("/TP")
    public ResponseEntity<String> getPictureTP() throws IOException {
        pictureLinksService.mapSheetAndSaveToDB("1mbEZtS329eu7miy42dWSIvECvjHeNosIOALv-S236a8","Trampolin park",1);
        pictureLinksService.updatePictureLinkInAC(1);
        pictureLinksService.updateEmailSentAndEmailOpened();
        pictureLinksService.writeToSheetBasedOnQuerry(1,"1mbEZtS329eu7miy42dWSIvECvjHeNosIOALv-S236a8");
        return ResponseEntity.ok("ok");
    }
    @Scheduled(cron = "0 35 */2 * * *")
    @GetMapping("/RU")
    public ResponseEntity<String> getPicturesRU() throws IOException{

        String sheetID = "17Q0gTlfaTx6dhQtsPWPFkqSOBLK4-xtUJeosPp0h5TQ";
        pictureLinksService.mapSheetAndSaveToDB(sheetID,"IZZIVI",5);
        pictureLinksService.updatePictureLinkInAC(5);
        pictureLinksService.updateEmailSentAndEmailOpened();
        pictureLinksService.writeToSheetBasedOnQuerry(5,sheetID);
        return ResponseEntity.ok("ok");
    }


    @Scheduled(cron = "0 40 */2 * * *")
    @GetMapping("/Karting")
    public ResponseEntity<String> getPicturesKarting() throws IOException{

        String sheetID = "1dRx3K0y_ANJl7nFlLeVgKg2CIg7EoMufMMn16S6giTE";
        pictureLinksService.mapSheetAndSaveToDB(sheetID,"Karting",2);
        pictureLinksService.updatePictureLinkInAC(2);
        pictureLinksService.updateEmailSentAndEmailOpened();
        pictureLinksService.writeToSheetBasedOnQuerry(2,sheetID);
        return ResponseEntity.ok("ok");
    }
    @Scheduled(cron = "0 25 */2 * * *")
    @GetMapping("/Arena")
    public ResponseEntity<String> getPicturesArena() throws IOException{

        String sheetID = "1UPzVSOn8aOGG-JAIiHPHGO9k7R8N4C0Lnqze9uQhlpY";
        pictureLinksService.mapSheetAndSaveToDB(sheetID,"Arena",3);
        pictureLinksService.updatePictureLinkInAC(3);
        pictureLinksService.updateEmailSentAndEmailOpened();
        pictureLinksService.writeToSheetBasedOnQuerry(3,sheetID);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() throws IOException {

        mailSendingService.sendBirthdayInvitationsEmail(1);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/invite")
    public ResponseEntity<String> invite() throws IOException {
        birthdayInvitationsService.mapAndSaveToInvitations("1mbEZtS329eu7miy42dWSIvECvjHeNosIOALv-S236a8","Trampolin park",1);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/testCalendar")
    public ResponseEntity<String> testCalendar() throws Exception {

       String url = googleCalendarInviteService.sendBirthdayInviteAndGetLink("test 2", LocalDateTime.of(LocalDate.of(2024,7,17), LocalTime.of(15,30))," lokacija","klavdija.levstek@woop.fun",3);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/responseTest")
    public ResponseEntity<String> checkResponse() {
        try {
            googleCalendarInviteService.checkGuestResponse("6gmflm8jghjlugtm1qm3b0s670", "matic.zigon@woop.fun");
            googleCalendarInviteService.checkGuestResponse("b7lul1pu94s9t8r3mpu1g5vres", "matic.zigon@woop.fun");
            googleCalendarInviteService.checkGuestResponse("s5v5at4mvbpcta8b46dtb5693o", "matic.zigon@woop.fun");

            /*Attendee klavdija.levstek@woop.fun response: accepted
            Attendee klavdija.levstek@woop.fun response: declined
            Attendee klavdija.levstek@woop.fun response: needsAction*/

            return ResponseEntity.ok("Check console for attendee responses");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking responses: " + e.getMessage());
        }
    }
}
