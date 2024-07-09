package com.maticz.BirthdaysDWH.controller;

import com.maticz.BirthdaysDWH.model.Birthdays;
import com.maticz.BirthdaysDWH.repository.BirthdaysRepository;
import com.maticz.BirthdaysDWH.service.ACService;
import com.maticz.BirthdaysDWH.service.BirthdaysService;
import com.maticz.BirthdaysDWH.service.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/dwh")
public class BirthdayController {

    @Autowired
    BirthdaysRepository birthdaysRepository;
    @Autowired
    BirthdaysService birthdaysService;

    @Autowired
    GoogleSheetsServiceImpl googleSheetsService;

    @Autowired
    BirthdayServiceImpl birthdayServiceImpl;

    @Autowired
    BirthdayCancelationServiceImpl birthdayCancelationServiceImpl;

    @Autowired
    PictureLinksServiceImpl pictureLinksService;

    @Autowired
    ACServiceImpl acService;


    Logger logger = LoggerFactory.getLogger(BirthdayController.class);



    @GetMapping("importCopyArena")
    public ResponseEntity<String> testR() throws IOException {


        birthdayServiceImpl.mapSheetCopyArenaAndSave("1mokK0_uqBRhMmJa6PM6uIjrXvhYDsBwLeptxD2pAvZA","Arena");

       return ResponseEntity.ok("ok");
    }

    @GetMapping("importCopyMS")
    public ResponseEntity<String> msCopy() throws IOException{

        birthdayServiceImpl.mapSheetCopyMS("1-vaNePIeLHGSibfbo6VOC0P5Xnkha3EmuU7D9cZd-aE","MURSKA SOBOTA");
        return ResponseEntity.ok("ok");
    }

    @GetMapping("importCopyMS2")
    public ResponseEntity<String> msCopy2() throws IOException{

        birthdayServiceImpl.mapSheetCopyMS("1YE4xdu5HKDKUXcrPSHkiUfmv6OmLbOgcOrObdGLxWYU","MURSKA SOBOTA");
        return ResponseEntity.ok("ok");
    }

    @GetMapping("importCopyTP1April")
    public ResponseEntity<String> tpCopy() throws IOException {

        birthdayServiceImpl.mapSheetCopyTP("1paR2Q5Ku8MHJWD5GvJxmM9EBZxCH4DIY9xx0vdkgt-4","Trampolin park");

        return ResponseEntity.ok("ok");
    }

    @GetMapping("importCopyTPBeforeApril")
    public ResponseEntity<String> tpCopymar() throws IOException {

        birthdayServiceImpl.mapSheetCopyTP("16ueoVFK3Eit6Nb7yVMLxm-0kkltDLiXCXsiFIDGEKy0","Trampolin park");

        return ResponseEntity.ok("ok");
    }

    @GetMapping("importCopytp2")
    public ResponseEntity<String> tpCopymard() throws IOException {

        birthdayServiceImpl.mapSheetTPSept("122aqo_h9yVuYi1Yrw92-OmuuPsXAs73D5ldT4i-qx18","Trampolin park");

        return ResponseEntity.ok("ok");
    }



    @GetMapping("importCopyKarting")
    public ResponseEntity<String> kartingCopy() throws IOException {

        birthdayServiceImpl.mapSheetCopyKarting("1D_hXaGrUcgFuDJl5Py3XINRcXzd7Mpw8kjMWQE2s0Dg","Karting");
        return ResponseEntity.ok("ok");
    }

    @GetMapping("importCopyMB")
    public ResponseEntity<String> MBCopy() throws IOException {

        birthdayServiceImpl.mapSheetCopyMB("1gRx5yh7o2zNrgoRaOOKS2NzG4xxQ5f1jFjCihnzsZCM"//"1Inzrs8q2fGOqlCxH6Ojbe5Gz3hWzm7YcoTVi4cZJU7Q"
                ,"Maribor");
        return ResponseEntity.ok("ok");
    }

    @GetMapping("importCopyRU")
    public ResponseEntity<String> rudnikCopy() throws IOException {

        birthdayServiceImpl.mapSheetCopyRU("1gv0G0FBnZYszYklySfFPtz3K6TbTCk7oJ-4bRI4rjpI","IZZIVI");
        return ResponseEntity.ok("ok");
    }
    @Scheduled(cron = "0 30 6 * * *")
    @GetMapping("importTP")
    public ResponseEntity<String> importTp() throws IOException {
        birthdayServiceImpl.mapSheetImportSaveToDB("1LZ4m7pCG0QVRijgJnrtWjCpiQIWwPVdLvoW5hc0nVLU","TP LJ",1);
        return ResponseEntity.ok("ok");
    }
    @Scheduled(cron = "0 33 6 * * *")
    @GetMapping("importKarting")
    public ResponseEntity<String> importKarting()    throws IOException {

        birthdayServiceImpl.mapSheetImportSaveToDB("1w7LpUDGSx7HbgvHMbpcp3Y_lBjOoJQU02R5q1q3fzy4","RD Karting",2);
        return ResponseEntity.ok("ok");
    }

    @Scheduled(cron = "0 35 6 * * *")
    @GetMapping("importArena")
    public ResponseEntity<String> importArena() throws IOException {

        birthdayServiceImpl.mapSheetImportSaveToDB("1NsKSd-YBp9Oar0Z_T6wDP6JXmiefi9ruzohzSSshjk4","RD Arena",3);
        return ResponseEntity.ok("ok");
    }
    @Scheduled(cron = "0 38 6 * * *")
    @GetMapping("importMB")
    public ResponseEntity<String> importMB() throws IOException {

        birthdayServiceImpl.mapSheetImportSaveToDB("1npZTif4BBFfGznvNL9A7icRuC9K7wPRTgzsQ0jPSA4E","RD MB",6);
        return ResponseEntity.ok("ok");
    }
    @Scheduled(cron = "0 40 6 * * *")
    @GetMapping("importRU")
    public ResponseEntity<String> importRU() throws IOException {

        birthdayServiceImpl.mapSheetImportSaveToDB("1PMfyZBdvZaerqX86GZoZF27HAr92OgoYm6bSsvBKGdo","RD Rudnik",5);
        return ResponseEntity.ok("ok");
    }
    @GetMapping("importMS")
    public ResponseEntity<String> importMS() throws IOException {

        birthdayServiceImpl.mapSheetImportSaveToDB("1arCEVrJfx-4hZSfR4_aFT4_9u-v7aT1nhCGsda_JM-I","RD MS", 4);
        return ResponseEntity.ok("ok");
    }


    @GetMapping("/importMSfrom{date}")
    public ResponseEntity<List<Birthdays>> importMSfrom(@RequestParam()String date) throws IOException {

       List<Birthdays> response =  birthdayServiceImpl.mapSheetImportSaveToDBFromDate("1arCEVrJfx-4hZSfR4_aFT4_9u-v7aT1nhCGsda_JM-I","RD MS", 4,date);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/importTPfrom{date}")
    public ResponseEntity<List<Birthdays>>  importTPfrom(@RequestParam() String date) throws IOException {
        List<Birthdays> response  = birthdayServiceImpl.mapSheetImportSaveToDBFromDate("1LZ4m7pCG0QVRijgJnrtWjCpiQIWwPVdLvoW5hc0nVLU","TP LJ",1,date);
                return ResponseEntity.ok(response);
    }


    @GetMapping("/importKartingfrom{date}")
    public ResponseEntity<List<Birthdays>>  importKarting(@RequestParam() String date) throws IOException {
        List<Birthdays> response = birthdayServiceImpl.mapSheetImportSaveToDBFromDate("1w7LpUDGSx7HbgvHMbpcp3Y_lBjOoJQU02R5q1q3fzy4","RD Karting",2,date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/importMBfrom{date}")
    public ResponseEntity<List<Birthdays>> importMBfrom(@RequestParam() String date) throws IOException {
        List<Birthdays> response =  birthdayServiceImpl.mapSheetImportSaveToDBFromDate("1npZTif4BBFfGznvNL9A7icRuC9K7wPRTgzsQ0jPSA4E","RD MB",6,date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/importRUfrom{date}")
    public ResponseEntity<List<Birthdays>>  importRUfrom(@RequestParam() String date) throws IOException {

        List<Birthdays> response = birthdayServiceImpl.mapSheetImportSaveToDBFromDate("1PMfyZBdvZaerqX86GZoZF27HAr92OgoYm6bSsvBKGdo","RD Rudnik",5,date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/importArenafrom{date}")
    public ResponseEntity<List<Birthdays>>  importArenafrom(@RequestParam() String date) throws IOException {

        List<Birthdays> response = birthdayServiceImpl.mapSheetImportSaveToDBFromDate("1NsKSd-YBp9Oar0Z_T6wDP6JXmiefi9ruzohzSSshjk4","RD Arena",3,date);
        return ResponseEntity.ok(response);
    }


    @Scheduled(cron = "0 55 6 * * *")
    @GetMapping("/importOdpovedArena")
    public ResponseEntity<String> importAOdpoved() throws IOException {
        birthdayCancelationServiceImpl.mapAndCopyCancelattion("1NsKSd-YBp9Oar0Z_T6wDP6JXmiefi9ruzohzSSshjk4","Odpoved Arena",3);
        return ResponseEntity.ok("ok");

    }

    @GetMapping("/importOdpovedTp")
    @Scheduled(cron = "0 57 6 * * *")
    public ResponseEntity<String> importTpOdpoved() throws IOException {
        birthdayCancelationServiceImpl.mapAndCopyCancelattion("1LZ4m7pCG0QVRijgJnrtWjCpiQIWwPVdLvoW5hc0nVLU","Odpoved",1);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/importOdpovedKarting")
    @Scheduled(cron = "0 59 6 * * *")
    public ResponseEntity<String> importKartingOdpoved() throws IOException {
        birthdayCancelationServiceImpl.mapAndCopyCancelattion("1w7LpUDGSx7HbgvHMbpcp3Y_lBjOoJQU02R5q1q3fzy4","Odpoved karting",2);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/importOdpovedMB")
    @Scheduled(cron = "0 57 6 * * *")
    public ResponseEntity<String> importMBOdpoved() throws IOException {
        birthdayCancelationServiceImpl.mapAndCopyCancelattion("1npZTif4BBFfGznvNL9A7icRuC9K7wPRTgzsQ0jPSA4E","Odpovedi MB",6);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/importOdpovedRU")
    @Scheduled(cron = "0 51 6 * * *")
    public ResponseEntity<String> importRUOdpoved() throws IOException {
        birthdayCancelationServiceImpl.mapAndCopyCancelattion("1PMfyZBdvZaerqX86GZoZF27HAr92OgoYm6bSsvBKGdo", "Odpoved rudnik", 5);
        return ResponseEntity.ok("ok");
    }


        @GetMapping("/odpovedicopyMB")
    public ResponseEntity<String> odpovedi() throws IOException {

        birthdayCancelationServiceImpl.copyCancellationFromCopySheets("1gRx5yh7o2zNrgoRaOOKS2NzG4xxQ5f1jFjCihnzsZCM","Odpovedi",6);
        return ResponseEntity.ok("ok");
        }

    @GetMapping("/odpovedicopyTP")
    public ResponseEntity<String> odpovedCopyTP() throws IOException {

        birthdayCancelationServiceImpl.copyCancellationFromCopySheets("122aqo_h9yVuYi1Yrw92-OmuuPsXAs73D5ldT4i-qx18","Odpovedi",1);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/odpovedicopyKarting")
    public ResponseEntity<String> odpovedKartingcopy() throws IOException {

        birthdayCancelationServiceImpl.copyCancellationFromCopySheets("1D_hXaGrUcgFuDJl5Py3XINRcXzd7Mpw8kjMWQE2s0Dg","ODPOVEDII",2);
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/odpovedicopyArena")
    public ResponseEntity<String> odpovediArenacopy() throws IOException {
        birthdayCancelationServiceImpl.copyCancellationFromCopySheets("1mokK0_uqBRhMmJa6PM6uIjrXvhYDsBwLeptxD2pAvZA", "Odpovedi",3);
        return ResponseEntity.ok("ok");
    }


    @GetMapping("/odpovedicopyMS")
            public ResponseEntity<String> odpovedioCopyMS() throws IOException {

        birthdayCancelationServiceImpl.copyCancellationFromCopySheets("1-vaNePIeLHGSibfbo6VOC0P5Xnkha3EmuU7D9cZd-aE","Odpovedi",4);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/odpovedicopyRU")
    public  ResponseEntity<String> odpovediCopyRU() throws IOException {

        birthdayCancelationServiceImpl.copyCancellationFromCopySheets("1gv0G0FBnZYszYklySfFPtz3K6TbTCk7oJ-4bRI4rjpI","Odpovedi",5);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/upcoming")
    @Scheduled(cron = "0 51 2 * * *")
    public ResponseEntity<String> upcomingTP() throws IOException {
        birthdayServiceImpl.mapAndSaveUpcomingBirthdays("1mbEZtS329eu7miy42dWSIvECvjHeNosIOALv-S236a8","Trampolin park",1);
        birthdayServiceImpl.mapAndSaveUpcomingBirthdays("1dRx3K0y_ANJl7nFlLeVgKg2CIg7EoMufMMn16S6giTE", "Karting",2);
        birthdayServiceImpl.mapAndSaveUpcomingBirthdays("1UPzVSOn8aOGG-JAIiHPHGO9k7R8N4C0Lnqze9uQhlpY","Arena",3);
        birthdayServiceImpl.mapAndSaveUpcomingBirthdays("17Q0gTlfaTx6dhQtsPWPFkqSOBLK4-xtUJeosPp0h5TQ","IZZIVI",5);
        birthdayServiceImpl.mapAndSaveUpcomingBirthdays("12kpvX_ui0yGlVW6BXeLAQYBntEtI264APyfTvpMSrSw","Maribor",6);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/te")
    public ResponseEntity<String> test44() throws IOException {

        birthdayServiceImpl.mapAndSaveUpcomingBirthdays("1UPzVSOn8aOGG-JAIiHPHGO9k7R8N4C0Lnqze9uQhlpY","Arena",3);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() throws IOException {
        birthdayServiceImpl.getDatesAndTimeForBirthdays("1mbEZtS329eu7miy42dWSIvECvjHeNosIOALv-S236a8","Trampolin park");
        return ResponseEntity.ok("ok");
    }




    @GetMapping("/writeTEST")
    public ResponseEntity<String> testWrite() throws IOException{
        acService.sendToACPictureLink("matic.zigon@woop.fun","test.com");
        return ResponseEntity.ok("ok");
    }

    }

