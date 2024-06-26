package com.maticz.BirthdaysDWH.controller;

import com.maticz.BirthdaysDWH.service.SummerCampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("AC")
public class SummerCampController {

    @Autowired
    SummerCampService summerCampService;

    @Scheduled(cron = "0 0 */2 * * *")  // na vsaki 2 uri
    @GetMapping("/summerCamp")
    ResponseEntity<String> test1() throws IOException {
        summerCampService.mapSheetHolidaysAndSaveToDBAndAC("1aK7ZyNmDwEtJkzHeevWfI2P2iV4mO9MHgUAq82ZZ8oI","Master prijave","A2:O400", 1);
        summerCampService.mapSheetHolidaysAndSaveToDBAndAC("1dGeMbGrTDMsxxENyxgAcvZ3L_mID1gXo4NkpgsUTPSY","Master prijave", "A2:O400",6);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/2")
    ResponseEntity<String> test2() throws IOException {
        summerCampService.copyValuesToSheet("1aK7ZyNmDwEtJkzHeevWfI2P2iV4mO9MHgUAq82ZZ8oI", "Master prijave" , "A2:O15");
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/3")
    ResponseEntity<String> test4() throws IOException {
        summerCampService.copy23data("1K2bksWGYiAPX-DjDyOMQ66vG4DsjC0wwW6J0gcDlpdE","Master prijave","A2:AA200",6);
        return ResponseEntity.ok("ok");
    }
}
