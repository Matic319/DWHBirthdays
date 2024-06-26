package com.maticz.BirthdaysDWH.service.impl;

import com.maticz.BirthdaysDWH.model.NPS;
import com.maticz.BirthdaysDWH.model.NPSBirthdays;
import com.maticz.BirthdaysDWH.repository.NPSBirthdaysRepository;
import com.maticz.BirthdaysDWH.repository.NPSRepository;
import com.maticz.BirthdaysDWH.service.NPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class NPSServiceImpl implements NPSService {

 @Autowired
 GoogleSheetsServiceImpl googleSheetsServiceImpl;

 @Autowired
    NPSRepository npsRepository;

 @Autowired
 NPSBirthdaysRepository npsBirthdaysRepository;

 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<NPS> mapAndSaveToDBNPS(String idSheet, String sheetName, String range) throws IOException {

        List<List<Object>> sheetData = googleSheetsServiceImpl.readSheetRangeFrom(idSheet,sheetName,range);

        List<NPS> resultList = null;

        resultList = new ArrayList<>();

        for( List<Object> row : sheetData) {

            LocalDate date = LocalDate.parse(row.get(0).toString(),formatter);
            String location = row.get(1).toString();
            Integer NPSScore = Integer.parseInt(row.get(2).toString());
            String email = row.get(4).toString();

            if(npsRepository.getCountByDateAndEmail(date,email) == 0){

                NPS nps = new NPS();

                nps.setDateNPS(date);
                nps.setNPS(NPSScore);
                nps.setEmail(email);
                nps.setLocation(location);

                npsRepository.save(nps);
                resultList.add(nps);

            }


        }
        return resultList;
    }

    @Override
    public List<NPSBirthdays> mapAndSaveToDBNPSBday2(String idSheet, String sheetName, String range) throws IOException {

        List<List<Object>> sheetData = googleSheetsServiceImpl.readSheetRangeFrom(idSheet, sheetName, range);

        List<NPSBirthdays> nps = null;
        nps = new ArrayList<>();

        for (List<Object> row : sheetData) {

            LocalDate date = LocalDate.parse(row.get(1).toString(), formatter);
            String email = row.get(0).toString();
            Integer NPS = Integer.parseInt(row.get(2).toString());
            String location;
            try {
                location = row.get(3).toString();
            } catch (IndexOutOfBoundsException e) {
                location = null;
            }
            String program;

            try {
                program = row.get(4).toString();
            } catch (IndexOutOfBoundsException e) {
                program = null;
            }

            String animator;

            try {
                animator = row.get(5).toString();
            } catch (IndexOutOfBoundsException e) {
                animator = null;
            }



            if (npsBirthdaysRepository.findByEmailAndDateNPSBDay(email,date).isEmpty()) {
                NPSBirthdays npsB = new NPSBirthdays();

                npsB.setBDayNPS(NPS);
                npsB.setLocation(location);
                npsB.setAnimator(animator);
                npsB.setBirthDayPartyType(program);
                npsB.setAnimator(animator);
                npsB.setEmail(email);
                npsB.setDateNPSBDay(date);

                npsBirthdaysRepository.save(npsB);
                nps.add(npsB);

            }
        }
        return nps;
    }
}
