package com.maticz.BirthdaysDWH.controller;

import com.maticz.BirthdaysDWH.model.NPS;
import com.maticz.BirthdaysDWH.model.NPSBirthdays;
import com.maticz.BirthdaysDWH.repository.BirthdaysRepository;
import com.maticz.BirthdaysDWH.repository.NPSBirthdaysRepository;
import com.maticz.BirthdaysDWH.repository.NPSRepository;
import com.maticz.BirthdaysDWH.service.NPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dwh")
@Component
public class NPSController {

    public NPSController(BirthdaysRepository birthdaysRepository) {
        this.birthdaysRepository = birthdaysRepository;
    }

    BirthdaysRepository birthdaysRepository;

    @Autowired
    NPSRepository npsRepository;

    @Autowired
    NPSBirthdaysRepository npsBirthdaysRepository;

    @Autowired
    NPSService npsService;

    @PostMapping("/NPS")
    public ResponseEntity<String> uploadNPS(@RequestBody List<NPS> json) {
        npsRepository.saveAll(json);
        return ResponseEntity.ok("Saved");
    }

    @PostMapping("/NPSBirthdays")
    public ResponseEntity<String> uploadNPSBDay(@RequestBody List<NPSBirthdays> json){
        npsBirthdaysRepository.saveAll(json);
        return ResponseEntity.ok("saved");
    }

    @Scheduled(cron = "0 45 6 * * *")
    @GetMapping("getNPS")

    public ResponseEntity<List<NPS>> getNPS() throws IOException {

        List<NPS> response = npsService.mapAndSaveToDBNPS("1hTQ9AbtgjGk6up-led4g0EvT6c2iYgreXNJdNqkyb7k","PODATKI","A13300:E30000");
        return ResponseEntity.ok(response);
    }



    @GetMapping("/getNPSBday")
    @Scheduled(cron = "0 48 6 * * *")
    public ResponseEntity<List<NPSBirthdays>> getNPSBday2() throws IOException {

        List<NPSBirthdays> result = npsService.mapAndSaveToDBNPSBday2("1w3JQE_fmw2q3L5E4I5qyf-cOCLfcHW8k6lOpO6Ac5hI","Sheet1","A2:F20000");
     return ResponseEntity.ok(result);

    }


}
