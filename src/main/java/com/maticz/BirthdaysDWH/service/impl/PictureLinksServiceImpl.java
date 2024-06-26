package com.maticz.BirthdaysDWH.service.impl;

import com.maticz.BirthdaysDWH.model.BirthdayPictures;
import com.maticz.BirthdaysDWH.repository.BirthdayPicturesRepository;
import com.maticz.BirthdaysDWH.repository.BirthdaysRepository;
import com.maticz.BirthdaysDWH.service.PictureLinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

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
    DateTimeFormatter formatterSQL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    Logger logger = Logger.getLogger(PictureLinksServiceImpl.class.getName());




    @Override
    public void mapSheetAndSaveToDB(String idSheet, String sheetName, Integer idLocation) throws IOException {

        List<List<Object>> sheetData = googleSheetsService.readSheetRangeFrom(idSheet,sheetName,"B3:AM400");

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
                    parentEmail = row.get(25).toString();
                    pictureLink = row.get(37).toString();
                    sendPhoto = Boolean.parseBoolean(row.get(16).toString());
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
                        birthdayPictures.setUpdateLink(0); // Spremen v 1 pol !
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
    List<Object[]> emailAndLinkList = birthdayPicturesRepository.getEmailAndLink();
    for (Object[] row : emailAndLinkList) {
        try {
            acService.sendToACPictureLink(row[0].toString(), row[1].toString());
            birthdayPicturesRepository.updateLink(idLocation, LocalDateTime.parse(row[2].toString(), formatterSQL), row[0].toString());
        } catch (HttpServerErrorException.BadGateway e) {
        }
    }
}
}
