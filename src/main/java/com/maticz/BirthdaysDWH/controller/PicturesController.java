package com.maticz.BirthdaysDWH.controller;

import com.maticz.BirthdaysDWH.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

    @GetMapping("/Arena")
    public ResponseEntity<String> getPicturesArena() throws IOException{
        pictureLinksService.mapSheetAndSaveToDB("1UPzVSOn8aOGG-JAIiHPHGO9k7R8N4C0Lnqze9uQhlpY","Arena",3);
        pictureLinksService.updateEmailSentAndEmailOpened();
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() throws IOException {
        pdfTextInsertionService.insertTextIntoPdf("C:\\Users\\Matic\\Desktop\\Vabilo_rojstni_dan_OSNOVNI FILE_2023.pdf","C:\\Users\\Matic\\Desktop\\test.pdf",
                "10","2024-06-06", "17:00","0311231231", "test",1);

        return ResponseEntity.ok("ok");
    }
}
