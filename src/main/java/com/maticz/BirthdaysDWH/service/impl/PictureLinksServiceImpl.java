package com.maticz.BirthdaysDWH.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maticz.BirthdaysDWH.model.BirthdayPictures;
import com.maticz.BirthdaysDWH.repository.BirthdayPicturesRepository;
import com.maticz.BirthdaysDWH.service.PictureLinksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PictureLinksServiceImpl implements PictureLinksService {

    @Autowired
    GoogleSheetsServiceImpl googleSheetsService;

    @Autowired
    BirthdayPicturesRepository birthdayPicturesRepository;

    @Autowired
            ACServiceImpl acService;


    DateTimeFormatter formatterDMYOnly = DateTimeFormatter.ofPattern("d. M. yyyy");
    DateTimeFormatter formatterHMSOnly = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter formatterHMOnly = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter formatterSQL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.S][.SS][.SSS]");
    Logger logger = LoggerFactory.getLogger(PictureLinksServiceImpl.class.getName());




    @Override
    public void mapSheetAndSaveToDB(String idSheet, String sheetName, Integer idLocation) throws IOException {

        List<List<Object>> sheetData = googleSheetsService.readSheetRangeFrom(idSheet,sheetName,"B3:AP400");

        for (List<Object> row : sheetData) {

            LocalDate dateFromWithoutTime;
            try {
                dateFromWithoutTime = LocalDate.parse(row.get(0).toString(), formatterDMYOnly);
            } catch (Exception e) {
                dateFromWithoutTime = LocalDate.of(2099,1,1);
            }
            if (dateFromWithoutTime.isBefore(LocalDate.now())) {
                String parentEmail;
                String pictureLink;
                Boolean sendPhoto;
                LocalDateTime dateFrom;
                LocalDateTime dateTo;


                try {
                    parentEmail = row.get(26).toString();
                    pictureLink = row.get(39).toString();
                    sendPhoto = Boolean.parseBoolean(row.get(17).toString());
                } catch (Exception e) {
                    parentEmail = null;
                    pictureLink = null;
                    sendPhoto = false;
                }
                if(parentEmail!= null && !parentEmail.isEmpty() && pictureLink!= null && sendPhoto) {
                    BirthdayPictures birthdayPictures = new BirthdayPictures();

                        try{
                            dateFrom = LocalDateTime.of(dateFromWithoutTime, LocalTime.parse(row.get(2).toString(), formatterHMOnly ));
                        } catch (DateTimeParseException e) {
                            try {
                                dateFrom = LocalDateTime.of(dateFromWithoutTime, LocalTime.parse(row.get(2).toString(), formatterHMSOnly));

                            }catch (DateTimeParseException f) {
                                dateFrom = LocalDateTime.of(dateFromWithoutTime,LocalTime.of(23,59,5,59));
                            }
                        }
                        try {
                            dateTo = LocalDateTime.of(dateFromWithoutTime, LocalTime.parse(row.get(3).toString(), formatterHMOnly));
                        }catch (DateTimeParseException e) {
                            try {
                                dateTo = LocalDateTime.of(dateFromWithoutTime, LocalTime.parse(row.get(3).toString(), formatterHMSOnly));

                            }catch (DateTimeParseException f) {
                                dateTo = LocalDateTime.of(dateFromWithoutTime,LocalTime.of(23,59,5,59));
                            }
                        }
                        birthdayPictures.setEmail(parentEmail);
                        birthdayPictures.setDateFrom(dateFrom);
                        birthdayPictures.setDateTo(dateTo);
                        birthdayPictures.setPictureLink(pictureLink);
                        birthdayPictures.setIdLocation(idLocation);
                        birthdayPictures.setImportTimestamp(LocalDateTime.now());
                        birthdayPictures.setUpdateLink(1);
                        birthdayPictures.setEmailOpened(0);
                        birthdayPictures.setEmailSentFromAC(0);
                        switch(idLocation) {
                            case 1 : birthdayPictures.setIdCampaign(516); break;
                            case 2 : birthdayPictures.setIdCampaign(522); break;
                            case 3 : birthdayPictures.setIdCampaign(572); break;
                            case 5 : birthdayPictures.setIdCampaign(560); break;
                            case 6 : birthdayPictures.setIdCampaign(1227); break;
                        }

                        logger.info(birthdayPictures.toString());
                        if(birthdayPicturesRepository.findByEmailAndDateFromAndIdLocation(birthdayPictures.getEmail(),birthdayPictures.getDateFrom(),birthdayPictures.getIdLocation()).isEmpty()) {
                            birthdayPicturesRepository.save(birthdayPictures);
                        }
                }
            }
        }
    }

    @Override
    public void updatePictureLinkInAC(Integer idLocation) throws IOException {
        List<Object[]> emailAndLinkList = birthdayPicturesRepository.getEmailAndLink(idLocation);
        logger.info(emailAndLinkList.toString());
        for (Object[] row : emailAndLinkList) {
            try {
                logger.info(row[0].toString() + " " + row[1].toString());
                acService.sendToACPictureLink(row[0].toString(), row[1].toString(),idLocation);
                LocalDateTime date = LocalDateTime.parse(row[2].toString(), formatterSQL);
                birthdayPicturesRepository.updateLink(idLocation, date, row[0].toString());
            } catch (HttpServerErrorException.BadGateway e) {
                logger.info("Bad gateway: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("Error updating picture link in AC: {}", e.getMessage());
            }
        }
    }

    @Override
    public void updateEmailSentAndEmailOpened() throws JsonProcessingException {
        List<Object[]> listToUpdate = birthdayPicturesRepository.getContactEmailAndDateFromAndIdLocationAndEmailOpenedAndSentWhereSentAndOpenedZero();

        for(Object[] row : listToUpdate) {
            if(row[5] != null) {
                birthdayPicturesRepository.updateEmailSentFromAC(row[0].toString(), LocalDateTime.parse(row[1].toString(),formatterSQL), Integer.parseInt(row[2].toString()));
            }
            if(row[4] != null) {
                birthdayPicturesRepository.updateEmailOpened(row[0].toString(), LocalDateTime.parse(row[1].toString(),formatterSQL), Integer.parseInt(row[2].toString()));
            }
            if(Integer.parseInt(row[6].toString()) == 0) {
                acService.updateEmailSentIfMatchFoundInActivities(row[0].toString(), LocalDateTime.parse(row[1].toString(), formatterSQL), Integer.valueOf(row[2].toString()));
            }
        }
    }

    @Override
    public void writeToSheetBasedOnQuerry(Integer idLocation, String sheetId) throws IOException {

        List<Object[]> values = birthdayPicturesRepository.getContactEmailAndDateFromAndIdLocationAndEmailOpenedAndSent(idLocation);

        List<List<Object>> listValues = new ArrayList<>();
        listValues.add(Arrays.asList("datum RD","email","poslan mail", "odprt mail", "posodobljeno"));
        listValues.add(Arrays.asList(" "," "," "," ",LocalDateTime.now().toString()));
        for (Object[] row : values) {
            String emailSent = switch (row[6].toString()) {
                case "1" -> "JA";
                case "0" -> "NE";
                default -> null;
            };

            String emailOpened = switch (row[7].toString()) {
                case "1" -> "JA";
                case "0" -> "NE";
                default -> null;
            };
            listValues.add(Arrays.asList(row[1].toString(), row[0].toString(), emailSent , emailOpened));
        }
        googleSheetsService.clearAndInsertValues(sheetId,"Slike","A1:O999",listValues);

    }
}

