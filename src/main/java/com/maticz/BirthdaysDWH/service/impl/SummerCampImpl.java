package com.maticz.BirthdaysDWH.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maticz.BirthdaysDWH.model.SummerCamp;
import com.maticz.BirthdaysDWH.repository.SummerCampRepository;
import com.maticz.BirthdaysDWH.service.SummerCampService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class SummerCampImpl implements SummerCampService {

    Logger logger = LoggerFactory.getLogger(SummerCampImpl.class);

    @Autowired
    GoogleSheetsServiceImpl googleSheetsService;

    @Autowired
    SummerCampRepository summerCampRepository;

    @Autowired
    ACServiceImpl acService;

    DateTimeFormatter formatterACTimestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    @Override
    public void mapSheetHolidaysAndSaveToDBAndAC(String idSheet, String sheetName, String range, Integer idLocation) throws IOException {

        summerCampRepository.deleteAllByIdLocation(idLocation);
        List<List<Object>> data = googleSheetsService.readSheetRangeFrom(idSheet, sheetName, range);

        data.forEach(row -> {
            SummerCamp smrcmp = new SummerCamp();
            logger.info(row.toString());
            LocalDateTime ACTimestamp = LocalDateTime.parse(row.get(0).toString(), formatterACTimestamp);

            boolean cancellation = false;
            Integer discount = 0;

            try {
                if(!row.get(6).toString().isEmpty()) {
                    discount = 1;
                }
            } catch (NullPointerException e) {
                discount = 0;
            }
            Integer daysCount = 5;

            String email = row.get(7).toString();
            String childName = row.get(1).toString();
            String campDateString = row.get(4).toString();
            String days = null;
            Integer daily ;
            try {
                 days = row.get(9).toString();
            }catch (NullPointerException ignored) {

            }
            switch (row.get(8).toString() )  {
                case "Ne" -> daily = 0;
                case "Da" -> daily = 1;
                default -> daily = 0;
            }
            logger.info(row.get(8).toString());

            try {
                String cancel = row.get(5).toString().trim();
                if (!cancel.isEmpty()) {
                    cancellation = true;
                }
            } catch (NullPointerException ignored) {
            }

            try {
                discount = Integer.parseInt(row.get(6).toString());
            } catch (NullPointerException | NumberFormatException ignored) {

            }

                smrcmp.setEmail(email);
                smrcmp.setCampDate(campDateString);
                smrcmp.setDiscount(discount);
                smrcmp.setChildName(childName);
                smrcmp.setCancel(0);
                smrcmp.setDailyCare(daily);
                smrcmp.setDays(days);
                smrcmp.setSubmitDate(ACTimestamp);

                if(daily == 1 ) {
                    daysToInt(days, smrcmp);
                    daysCount = smrcmp.getMon() + smrcmp.getTue() + smrcmp.getWed() + smrcmp.getThu() + smrcmp.getFri();
                } else {
                    smrcmp.setMon(1);
                    smrcmp.setTue(1);
                    smrcmp.setWed(1);
                    smrcmp.setThu(1);
                    smrcmp.setFri(1);
                }

                smrcmp.setDaysCount(daysCount);

                setDateFrom(campDateString, smrcmp);

                setDateTo(campDateString, smrcmp);

                smrcmp.setDiscount(discount);
                smrcmp.setIdLocation(idLocation);

                if(cancellation){
                    smrcmp.setCancel(1);
                }else {
                    smrcmp.setCancel(0);
                }
                    summerCampRepository.save(smrcmp);
        });

        HashSet<String> cancellations = new HashSet<>();

        summerCampRepository.getClientsThatCancelled(idLocation).forEach(row -> {
            try {
                if(cancellations.contains(row[1].toString())){
                    Thread.sleep(20000);
                    acService.sendToAC(row[1].toString(),row[0].toString(),idLocation,true);
                } else {
                    cancellations.add(row[1].toString());
                }
                acService.sendToAC(row[1].toString(),row[0].toString(),idLocation,false);

            } catch (JsonProcessingException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        }

    @Override
    public void copyValuesToSheet(String idSheet, String sheetName, String range) throws IOException {

        List<List<Object>> data = googleSheetsService.readSheetRangeFrom(idSheet, sheetName, range);
        List<List<Object>> cDate1 = googleSheetsService.readSheetRangeFrom(idSheet, "1.termin","A2:O100");
        Map<String,List<Object>> campDateAndValues = new HashMap<>();

    }

    @Override
    public void copy23data(String idSheet, String sheetName, String range, Integer idLocation) throws IOException {
        List<List<Object>> data = googleSheetsService.readSheetRangeFrom(idSheet, sheetName, range);

        data.forEach(row -> {

            SummerCamp cmp = new SummerCamp();
            Boolean dontSave = false;

            cmp.setSubmitDate(LocalDateTime.parse(row.get(0).toString(),formatterACTimestamp));
            setDateFrom2023(row.get(4).toString(), cmp);
            if(Objects.equals(cmp.getDateFrom(), LocalDate.of(2023, 1, 1))){
                dontSave = true;
            }
            setDateTo2023(row.get(4).toString(),cmp);
            cmp.setChildName(row.get(1).toString());
            cmp.setEmail(row.get(6).toString());
            cmp.setIdLocation(idLocation);
            cmp.setCampDate(row.get(4).toString());

            if(row.get(11).toString().isEmpty()){
                cmp.setDailyCare(0);
            }else {
                cmp.setDailyCare(1);
            }

            if(!dontSave){
                summerCampRepository.save(cmp);
            }

        });

    }

    private static void daysToInt(String days, SummerCamp smrcmp) {
        if (days.contains("Ponedeljek")) {
            smrcmp.setMon(1);
        }else {
            smrcmp.setMon(0);
        }
        if(days.contains("Torek")) {
            smrcmp.setTue(1);
        }else {
            smrcmp.setTue(0);
        }
        if (days.contains("Sreda")) {
            smrcmp.setWed(1);
        } else {
            smrcmp.setWed(0);
        }
        if (days.contains("Četrtek")) {
            smrcmp.setThu(1);
        } else {
            smrcmp.setThu(0);
        }
        if (days.contains("Petek")){
            smrcmp.setFri(1);
        } else {
            smrcmp.setFri(0);
        }
    }

    private static void setDateTo(String campDateString, SummerCamp smrcmp) {
        switch (campDateString) {
            case "junij - 26.6. -28.6." -> smrcmp.setDateTo(LocalDate.of(2024,6,28));
            case "julij - 1.7. -5.7." -> smrcmp.setDateTo(LocalDate.of(2024,7,5));
            case "julij - 8.7. -12.7." -> smrcmp.setDateTo(LocalDate.of(2024,7,12));
            case "julij - 15.7. -19.7." -> smrcmp.setDateTo(LocalDate.of(2024,7,19));
            case "julij - 22.7. - 26.7." -> smrcmp.setDateTo(LocalDate.of(2024,7,26));
            case "julij - 29.7. -2.8." -> smrcmp.setDateTo(LocalDate.of(2024,8,2));
            case "avgust - 5.8. -9.8." -> smrcmp.setDateTo(LocalDate.of(2024,8,9));
            case "avgust - 12.8. - 14.8. in 16.8" -> smrcmp.setDateTo(LocalDate.of(2024,8,16));
            case "avgust - 19.8. -23.8." -> smrcmp.setDateTo(LocalDate.of(2024,8,23));
            case "avgust - 26.8. -30.8." -> smrcmp.setDateTo(LocalDate.of(2024,8,30));
        }
    }

    private static void setDateFrom(String campDateString, SummerCamp smrcmp) {
        switch (campDateString) {
            case "junij - 26.6. -28.6." -> smrcmp.setDateFrom(LocalDate.of(2024,6,26));
            case "julij - 1.7. -5.7." -> smrcmp.setDateFrom(LocalDate.of(2024,7,1));
            case "julij - 8.7. -12.7." -> smrcmp.setDateFrom(LocalDate.of(2024,7,8));
            case "julij - 15.7. -19.7." -> smrcmp.setDateFrom(LocalDate.of(2024,7,15));
            case "julij - 22.7. - 26.7." -> smrcmp.setDateFrom(LocalDate.of(2024,7,22));
            case "julij - 29.7. -2.8." -> smrcmp.setDateFrom(LocalDate.of(2024,7,29));
            case "avgust - 5.8. -9.8." -> smrcmp.setDateFrom(LocalDate.of(2024,8,5));
            case "avgust - 12.8. - 14.8. in 16.8" -> smrcmp.setDateFrom(LocalDate.of(2024,8,12));
            case "avgust - 19.8. -23.8." -> smrcmp.setDateFrom(LocalDate.of(2024,8,19));
            case "avgust - 26.8. -30.8." -> smrcmp.setDateFrom(LocalDate.of(2024,8,26));
        }
    }

    private static void setDateFrom2023(String campDateString, SummerCamp cmp) {
        switch (campDateString) {
            case "junij - 26.6. - 30.6." -> cmp.setDateFrom(LocalDate.of(2023,6,26));
            case "julij - 31.7. - 4.8." -> cmp.setDateFrom(LocalDate.of(2023,7,31));
            case "julij - 3.7. - 7.7." -> cmp.setDateFrom(LocalDate.of(2023,7,3));
            case "julij - 24.7. - 28.7." -> cmp.setDateFrom(LocalDate.of(2023,7,24));
            case "julij - 17.7. - 21.7." -> cmp.setDateFrom(LocalDate.of(2023,7,17));
            case "julij - 10.7. - 14.7." -> cmp.setDateFrom(LocalDate.of(2023,7,10));
            case "avgust - 7.8. - 11.8." -> cmp.setDateFrom(LocalDate.of(2023,8,7));
            case "avgust - 28.8. - 31.8." -> cmp.setDateFrom(LocalDate.of(2023,8,28));
            case "avgust - 21.8. - 25.8." -> cmp.setDateFrom(LocalDate.of(2023,8,21));
            default -> cmp.setDateFrom(LocalDate.of(2023,1,1));
        }
    }

    private static void setDateTo2023(String campDateString , SummerCamp cmp) {
        switch (campDateString) {
            case "junij - 26.6. - 30.6." -> cmp.setDateTo(LocalDate.of(2023,6,30));
            case "julij - 31.7. - 4.8." -> cmp.setDateTo(LocalDate.of(2023,8,4));
            case "julij - 3.7. - 7.7." -> cmp.setDateTo(LocalDate.of(2023,7,7));
            case "julij - 24.7. - 28.7." -> cmp.setDateTo(LocalDate.of(2023,7,28));
            case "julij - 17.7. - 21.7." -> cmp.setDateTo(LocalDate.of(2023,7,21));
            case "julij - 10.7. - 14.7." -> cmp.setDateTo(LocalDate.of(2023,7,14));
            case "avgust - 7.8. - 11.8." -> cmp.setDateTo(LocalDate.of(2023,8,11));
            case "avgust - 28.8. - 31.8." -> cmp.setDateTo(LocalDate.of(2023,8,31));
            case "avgust - 21.8. - 25.8." -> cmp.setDateTo(LocalDate.of(2023,8,25));
            default -> cmp.setDateFrom(LocalDate.of(2023,1,1));
        }
    }

}
