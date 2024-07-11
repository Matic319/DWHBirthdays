package com.maticz.BirthdaysDWH.service.impl;

import com.maticz.BirthdaysDWH.model.BirthdayInvitations;
import com.maticz.BirthdaysDWH.repository.BirthdayInvitationsRepository;
import com.maticz.BirthdaysDWH.service.BirthdayInvitationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class BirthdayInvitationsServiceImpl implements BirthdayInvitationsService {

    @Autowired
    GoogleSheetsServiceImpl googleSheetsService;

    @Autowired
     BirthdayInvitationsRepository birthdayInvitationsRepository;

    private final Logger logger = LoggerFactory.getLogger(BirthdayInvitationsServiceImpl.class);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. M. yyyy HH:mm:ss");
    DateTimeFormatter formatterDMYOnly = DateTimeFormatter.ofPattern("d. M. yyyy");
    DateTimeFormatter formatterHM = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter formatterHMS = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void mapAndSaveToInvitations(String sheetId, String sheetName, Integer idLocation) throws IOException {
        List<List<Object>> sheetData= googleSheetsService.readSheetRangeFrom(sheetId, sheetName,"B3:AB1200");

        sheetData.forEach(row -> {
            LocalDate date = LocalDate.parse(row.get(0).toString(),formatterDMYOnly);

            if(date.isAfter(LocalDate.now())) {
                Boolean sendInvitation = Boolean.valueOf(row.get(18).toString());
                if(sendInvitation){
                    String email = row.get(25) != null ? row.get(25).toString() : ""; // če je prazn slucajno
                    if(email.contains("@")){
                        LocalTime time = null;
                        try{
                            time = LocalTime.parse(row.get(2).toString(),formatterHM);
                        }catch (DateTimeParseException e){
                            time = LocalTime.parse(row.get(2).toString(),formatterHMS);
                        }

                        String childName = row.get(19).toString();
                        Integer age = Integer.parseInt(row.get(22).toString());
                        String phone = row.get(26).toString();
                        LocalDateTime dateFrom = LocalDateTime.of(date, time);

                        if(birthdayInvitationsRepository.findByEmailAndDateFromAndIdLocationAndChildName(email,dateFrom,idLocation, childName).isEmpty()) {
                            setBdayInvitationAndSave(idLocation, email, dateFrom, childName, age, phone);
                        }

                    }
                }
            }
        });
    }

    private void setBdayInvitationAndSave(Integer idLocation, String email, LocalDateTime dateFrom, String childName, Integer age, String phone) {
        BirthdayInvitations bdInv = new BirthdayInvitations();
        bdInv.setEmail(email);
        bdInv.setDateFrom(dateFrom);
        bdInv.setChildName(childName);
        bdInv.setAge(age);
        bdInv.setImportTimestamp(LocalDateTime.now());
        bdInv.setIdLocation(idLocation);
        bdInv.setPhone(phone);
        bdInv.setEmailSent(0);
        birthdayInvitationsRepository.save(bdInv);
    }


}