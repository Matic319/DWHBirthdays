package com.maticz.BirthdaysDWH.service.impl;


import com.maticz.BirthdaysDWH.model.Birthdays;
import com.maticz.BirthdaysDWH.model.ErrorLog;
import com.maticz.BirthdaysDWH.repository.BirthdaysRepository;
import com.maticz.BirthdaysDWH.repository.ErrorLogRepository;
import com.maticz.BirthdaysDWH.service.BirthdaysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BirthdayServiceImpl implements BirthdaysService {

    private final BirthdaysRepository birthdaysRepository;

    BirthdayServiceImpl(BirthdaysRepository birthdaysRepository) {
        this.birthdaysRepository = birthdaysRepository;
    }

    Logger logger = LoggerFactory.getLogger(BirthdayServiceImpl.class);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. M. yyyy HH:mm:ss");
    DateTimeFormatter formatterDMYOnly = DateTimeFormatter.ofPattern("d. M. yyyy");

    DateTimeFormatter formatterHMSOnly = DateTimeFormatter.ofPattern("HH:mm:ss");

    DateTimeFormatter formatterHMOnly = DateTimeFormatter.ofPattern("HH:mm");

    DateTimeFormatter formatterString = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    GoogleSheetsServiceImpl googleSheetsService;

    @Autowired
    ErrorLogRepository errorLogRepository;



    @Override
    public void mapSheetCopyArenaAndSave(String sheetID, String sheetName) throws IOException {
        List<List<Object>> sheetData = googleSheetsService.readSheetRange(sheetID, sheetName);

        for (List<Object> row : sheetData) {

            Integer active = Integer.parseInt(row.get(88).toString());
            Boolean dontSave = false;

            if (active == 1) {

                Integer idBDayProgType = Integer.parseInt(row.get(85).toString());
                Integer idPlace = Integer.parseInt(row.get(87).toString());
                Integer idLocation = Integer.parseInt(row.get(89).toString());
                /*Long idBDay;
                try {
                    idBDay = Long.parseLong(row.get(90).toString());
                } catch (NumberFormatException e) {
                    idBDay = 0L;
                    dontSave = true;
                }*/

                String input = row.get(91).toString();
                input = input.replace('−', '-');

                Integer idContactResType;
                try {
                    idContactResType = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    idContactResType = -1;
                }
                LocalDateTime dateFrom;

                try {
                    dateFrom = LocalDateTime.parse(row.get(92).toString(), formatter);

                } catch (DateTimeParseException e) {
                    dateFrom = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }

                LocalDateTime dateTo;

                try {
                    dateTo = LocalDateTime.parse(row.get(93).toString(), formatter);
                } catch (DateTimeException e) {

                    dateTo = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }


                String duration = row.get(7).toString();
                String birthdayProgType = row.get(6).toString();
                String parentEmail = row.get(37).toString();
                String parentFirstName = row.get(39).toString();
                String parentLastName = row.get(40).toString();
                String childFirstName = row.get(31).toString();
                String childLastName = row.get(32).toString();
                Integer childBDayAge;
                try {
                    childBDayAge = Integer.parseInt(row.get(34).toString());
                } catch (NumberFormatException e) {
                    childBDayAge = -1;
                }
                Integer participantCount;
                try {
                    participantCount = Integer.parseInt(row.get(33).toString());
                } catch (NumberFormatException e) {
                    participantCount = -1;
                }
                String animator = row.get(24).toString();
                String partyPlaceName = row.get(21).toString();
                String extraAnimator = row.get(22).toString();

                Birthdays birthday = new Birthdays();

                birthday.setActive(active);
                //birthday.setIdBirthday(idBDay);
                birthday.setAnimator(animator);
                birthday.setBirthdayProgType(birthdayProgType);
                birthday.setChildBDayAge(childBDayAge);
                birthday.setDateTo(dateTo);
                birthday.setDateFrom(dateFrom);
                birthday.setChildFirstName(childFirstName);
                birthday.setChildLastName(childLastName);
                birthday.setDodatniAnimator(extraAnimator);
                birthday.setDuration(duration);
                birthday.setIdBDayProgType(idBDayProgType);
                birthday.setIdPlace(idPlace);
                birthday.setIdContactResType(idContactResType);
                birthday.setParticipantCount(participantCount);
                birthday.setParentEmail(parentEmail);
                birthday.setParentFirstName(parentFirstName);
                birthday.setParentLastName(parentLastName);
                birthday.setIdLocation(idLocation);
                birthday.setIdPartyPlaceName(partyPlaceName);


               // if (birthdaysRepository.findByIdBirthday(idBDay).isEmpty() && !dontSave) {

                    birthdaysRepository.save(birthday);

               // }

            }

        }
    }

    @Override
    public void mapSheetCopyMS(String sheetId, String sheetName) throws IOException {

        List<List<Object>> sheetData = googleSheetsService.readSheetRange(sheetId, sheetName);

        logger.info(sheetData.toString());
        for (List<Object> row : sheetData) {
            Boolean dontSave = false;
            Integer active;


            logger.info(row.toString());
            try {
                active = Integer.parseInt(row.get(83).toString());
            } catch (IndexOutOfBoundsException e) {
                active = 2;
            }

            if (active == 1) {

                Integer idBDayProgType = Integer.parseInt(row.get(80).toString());
                Integer idLocation = Integer.parseInt(row.get(84).toString());
               /* Long idBDay;
                try {
                    idBDay = Long.parseLong(row.get(85).toString());
                } catch (NumberFormatException e) {
                    idBDay = 0L;
                    dontSave = true;
                }*/

                String input = row.get(86).toString();
                input = input.replace('−', '-');

                Integer idContactResType;
                try {
                    idContactResType = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    idContactResType = -1;
                }
                LocalDateTime dateFrom;

                try {
                    dateFrom = LocalDateTime.parse(row.get(87).toString(), formatter);

                } catch (DateTimeParseException e) {
                    dateFrom = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }

                LocalDateTime dateTo;

                try {
                    dateTo = LocalDateTime.parse(row.get(88).toString(), formatter);
                } catch (DateTimeException e) {

                    dateTo = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }


                String duration = row.get(8).toString();
                String birthdayProgType = row.get(6).toString();
                String parentEmail = row.get(26).toString();
                String parentFirstName = row.get(28).toString();
                String parentLastName = row.get(29).toString();
                String childFirstName = row.get(20).toString();
                String childLastName = row.get(21).toString();
                Integer childBDayAge;
                try {
                    childBDayAge = Integer.parseInt(row.get(23).toString());
                } catch (NumberFormatException e) {
                    childBDayAge = -1;
                }
                Integer participantCount;
                try {
                    participantCount = Integer.parseInt(row.get(22).toString());
                } catch (NumberFormatException e) {
                    participantCount = -1;
                }
                String animator = row.get(11).toString();

                Birthdays birthday = new Birthdays();

                birthday.setActive(active);
                //birthday.setIdBirthday(idBDay);
                birthday.setAnimator(animator);
                birthday.setBirthdayProgType(birthdayProgType);
                birthday.setChildBDayAge(childBDayAge);
                birthday.setDateTo(dateTo);
                birthday.setDateFrom(dateFrom);
                birthday.setChildFirstName(childFirstName);
                birthday.setChildLastName(childLastName);
                birthday.setDuration(duration);
                birthday.setIdBDayProgType(idBDayProgType);
                birthday.setIdContactResType(idContactResType);
                birthday.setParticipantCount(participantCount);
                birthday.setParentEmail(parentEmail);
                birthday.setParentFirstName(parentFirstName);
                birthday.setParentLastName(parentLastName);
                birthday.setIdLocation(idLocation);


                if (!dontSave) {
                    birthdaysRepository.save(birthday);

                }

            }


        }
    }

    @Override
    public void mapSheetCopyTP(String sheetId, String sheetName) throws IOException {

        List<List<Object>> sheetData = googleSheetsService.readSheetRange(sheetId, sheetName);

        for (List<Object> row : sheetData) {
            Boolean dontSave = false;
            Integer active;
            try {
            active = Integer.parseInt(row.get(58).toString());}
            catch (NumberFormatException e) {
                dontSave = true;
                active = 2;
            }

            if (active == 1) {

                Integer idBDayProgType = Integer.parseInt(row.get(55).toString());
                Integer idPlace = Integer.parseInt(row.get(57).toString());
                Integer idLocation = Integer.parseInt(row.get(59).toString());
               /* Long idBDay;
                try {
                    idBDay = Long.parseLong(row.get(60).toString());
                } catch (NumberFormatException e) {
                    idBDay = 0L;
                    dontSave = true;
                }
*/
                String input = row.get(61).toString();
                input = input.replace('−', '-');

                Integer idContactResType;
                try {
                    idContactResType = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    idContactResType = -1;
                }
                LocalDateTime dateFrom;

                try {
                    dateFrom = LocalDateTime.parse(row.get(62).toString(), formatter);

                } catch (DateTimeParseException e) {
                    dateFrom = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }

                LocalDateTime dateTo;

                try {
                    dateTo = LocalDateTime.parse(row.get(63).toString(), formatter);
                } catch (DateTimeException e) {

                    dateTo = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }


                String duration = row.get(8).toString();
                String birthdayProgType = row.get(6).toString();
                String parentEmail = row.get(35).toString();
                String parentFirstName = row.get(37).toString();
                String parentLastName = row.get(38).toString();
                String childFirstName = row.get(29).toString();
                String childLastName = row.get(30).toString();
                String partyProg = row.get(7).toString();
                Integer idParty = Integer.parseInt(row.get(55).toString());

                Integer childBDayAge;
                try {
                    childBDayAge = Integer.parseInt(row.get(32).toString());
                } catch (NumberFormatException e) {
                    childBDayAge = -1;
                }
                Integer participantCount;
                try {
                    participantCount = Integer.parseInt(row.get(31).toString());
                } catch (NumberFormatException e) {
                    participantCount = -1;
                }
                String animator = row.get(22).toString();
                String partyPlaceName = row.get(21).toString();

                Birthdays birthday = new Birthdays();

                birthday.setActive(active);
                birthday.setAnimator(animator);
                birthday.setBirthdayProgType(birthdayProgType);
                birthday.setChildBDayAge(childBDayAge);
                birthday.setDateTo(dateTo);
                birthday.setDateFrom(dateFrom);
                birthday.setChildFirstName(childFirstName);
                birthday.setChildLastName(childLastName);
                birthday.setDuration(duration);
                birthday.setIdBDayProgType(idBDayProgType);
                birthday.setIdPlace(idPlace);
                birthday.setIdContactResType(idContactResType);
                birthday.setParticipantCount(participantCount);
                birthday.setParentEmail(parentEmail);
                birthday.setParentFirstName(parentFirstName);
                birthday.setParentLastName(parentLastName);
                birthday.setIdLocation(idLocation);
                birthday.setIdPartyPlaceName(partyPlaceName);
                birthday.setBirthdayPartyType(partyProg);
                birthday.setIdBDayPartyType(idParty);
                birthday.setIdBDayProgType(idBDayProgType);



                if (!dontSave) {

                    birthdaysRepository.save(birthday);


                }

            }
        }
    }

    @Override
    public void mapSheetTPSept(String sheetId, String sheetName) throws IOException {

        List<List<Object>> sheetData = googleSheetsService.readSheetRange(sheetId, sheetName);

        for (List<Object> row : sheetData) {

            Integer active = Integer.parseInt(row.get(58).toString());
            Boolean dontSave = false;

            if (active == 1) {

                Integer idBDayProgType = Integer.parseInt(row.get(55).toString());
                Integer idPlace = Integer.parseInt(row.get(57).toString());
                Integer idLocation = Integer.parseInt(row.get(59).toString());
                /*Long idBDay;
                try {
                    idBDay = Long.parseLong(row.get(90).toString());
                } catch (NumberFormatException e) {
                    idBDay = 0L;
                    dontSave = true;
                }*/

                String input = row.get(61).toString();
                input = input.replace('−', '-');

                Integer idContactResType;
                try {
                    idContactResType = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    idContactResType = -1;
                }
                LocalDateTime dateFrom;

                try {
                    dateFrom = LocalDateTime.parse(row.get(62).toString(), formatter);

                } catch (DateTimeParseException e) {
                    dateFrom = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }

                LocalDateTime dateTo;

                try {
                    dateTo = LocalDateTime.parse(row.get(63).toString(), formatter);
                } catch (DateTimeException e) {

                    dateTo = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }


                String duration = row.get(8).toString();
                String birthdayProgType = row.get(6).toString();
                String parentEmail = row.get(26).toString();
                String parentFirstName = row.get(28).toString();
                String parentLastName = row.get(29).toString();
                String childFirstName = row.get(20).toString();
                String childLastName = row.get(21).toString();
                Integer childBDayAge;
                try {
                    childBDayAge = Integer.parseInt(row.get(23).toString());
                } catch (NumberFormatException e) {
                    childBDayAge = -1;
                }
                Integer participantCount;
                try {
                    participantCount = Integer.parseInt(row.get(22).toString());
                } catch (NumberFormatException e) {
                    participantCount = -1;
                }
                String animator = row.get(11).toString();
                String partyPlaceName = row.get(10).toString();

                Birthdays birthday = new Birthdays();

                birthday.setActive(active);
                //birthday.setIdBirthday(idBDay);
                birthday.setAnimator(animator);
                birthday.setBirthdayProgType(birthdayProgType);
                birthday.setChildBDayAge(childBDayAge);
                birthday.setDateTo(dateTo);
                birthday.setDateFrom(dateFrom);
                birthday.setChildFirstName(childFirstName);
                birthday.setChildLastName(childLastName);
                birthday.setDuration(duration);
                birthday.setIdBDayProgType(idBDayProgType);
                birthday.setIdPlace(idPlace);
                birthday.setIdContactResType(idContactResType);
                birthday.setParticipantCount(participantCount);
                birthday.setParentEmail(parentEmail);
                birthday.setParentFirstName(parentFirstName);
                birthday.setParentLastName(parentLastName);
                birthday.setIdLocation(idLocation);
                birthday.setIdPartyPlaceName(partyPlaceName);


                // if (birthdaysRepository.findByIdBirthday(idBDay).isEmpty() && !dontSave) {

                birthdaysRepository.save(birthday);

                // }

            }

        }

    }

    @Override
    public void mapSheetCopyKarting(String sheetId, String sheetName) throws IOException {

        List<List<Object>> sheetData = googleSheetsService.readSheetRange(sheetId, sheetName);

        for (List<Object> row : sheetData) {

            Integer active = Integer.parseInt(row.get(52).toString());
            Boolean dontSave = false;

            if (active == 1) {

                Integer idBDayProgType = Integer.parseInt(row.get(49).toString());
                Integer idPlace = Integer.parseInt(row.get(51).toString());
                Integer idLocation = Integer.parseInt(row.get(53).toString());
                /*Long idBDay;
                try {
                    idBDay = Long.parseLong(row.get(54).toString());
                } catch (NumberFormatException e) {
                    idBDay = 0L;
                    dontSave = true;
                }*/

                String input = row.get(55).toString();
                input = input.replace('−', '-');

                Integer idContactResType;
                try {
                    idContactResType = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    idContactResType = -1;
                }
                LocalDateTime dateFrom;

                try {
                    dateFrom = LocalDateTime.parse(row.get(56).toString(), formatter);

                } catch (DateTimeParseException e) {
                    dateFrom = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }

                LocalDateTime dateTo;

                try {
                    dateTo = LocalDateTime.parse(row.get(57).toString(), formatter);
                } catch (DateTimeException e) {

                    dateTo = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }


                String duration = row.get(8).toString();
                String birthdayProgType = row.get(6).toString();
                String parentEmail = row.get(35).toString();
                String parentFirstName = row.get(37).toString();
                String parentLastName = row.get(38).toString();
                String childFirstName = row.get(29).toString();
                String childLastName = row.get(30).toString();
                Integer childBDayAge;
                try {
                    childBDayAge = Integer.parseInt(row.get(32).toString());
                } catch (NumberFormatException e) {
                    childBDayAge = -1;
                }
                Integer participantCount;
                try {
                    participantCount = Integer.parseInt(row.get(31).toString());
                } catch (NumberFormatException e) {
                    participantCount = -1;
                }
                String animator = row.get(22).toString();
                String partyPlaceName = row.get(21).toString();

                Birthdays birthday = new Birthdays();

                birthday.setActive(active);
                //birthday.setIdBirthday(idBDay);
                birthday.setAnimator(animator);
                birthday.setBirthdayProgType(birthdayProgType);
                birthday.setChildBDayAge(childBDayAge);
                birthday.setDateTo(dateTo);
                birthday.setDateFrom(dateFrom);
                birthday.setChildFirstName(childFirstName);
                birthday.setChildLastName(childLastName);
                birthday.setDuration(duration);
                birthday.setIdBDayProgType(idBDayProgType);
                birthday.setIdPlace(idPlace);
                birthday.setIdContactResType(idContactResType);
                birthday.setParticipantCount(participantCount);
                birthday.setParentEmail(parentEmail);
                birthday.setParentFirstName(parentFirstName);
                birthday.setParentLastName(parentLastName);
                birthday.setIdLocation(idLocation);
                birthday.setIdPartyPlaceName(partyPlaceName);


                if (!dontSave) {

                   // if (birthdaysRepository.findByParentEmailAndDateFrom(parentEmail,dateFrom).isEmpty() ){
                        birthdaysRepository.save(birthday);
                    //}



                }

            }

        }
    }

    @Override
    public void mapSheetCopyMB(String sheetId, String sheetName) throws IOException {
        List<List<Object>> sheetData = googleSheetsService.readSheetRange(sheetId, sheetName);

        for (List<Object> row : sheetData) {

            Integer active = Integer.parseInt(row.get(81).toString());
            Boolean dontSave = false;

            if (active == 1) {

                Integer idBDayProgType = Integer.parseInt(row.get(78).toString());
                Integer idPlace = Integer.parseInt(row.get(80).toString());
                Integer idLocation = Integer.parseInt(row.get(82).toString());
                /*Long idBDay;
                try {
                    idBDay = Long.parseLong(row.get(83).toString());
                } catch (NumberFormatException e) {
                    idBDay = 0L;
                    dontSave = true;
                }*/

                String input = row.get(84).toString();
                input = input.replace('−', '-');

                Integer idContactResType;
                try {
                    idContactResType = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    idContactResType = -1;
                }
                LocalDateTime dateFrom;

                try {
                    dateFrom = LocalDateTime.parse(row.get(85).toString(), formatter);

                } catch (DateTimeParseException e) {
                    dateFrom = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }

                LocalDateTime dateTo;

                try {
                    dateTo = LocalDateTime.parse(row.get(86).toString(), formatter);
                } catch (DateTimeException e) {

                    dateTo = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }


                String duration = row.get(8).toString();
                String birthdayProgType = row.get(6).toString();
                String parentEmail = row.get(35).toString();
                String parentFirstName = row.get(37).toString();
                String parentLastName = row.get(38).toString();
                String childFirstName = row.get(29).toString();
                String childLastName = row.get(30).toString();
                Integer childBDayAge;
                try {
                    childBDayAge = Integer.parseInt(row.get(32).toString());
                } catch (NumberFormatException e) {
                    childBDayAge = -1;
                }
                Integer participantCount;
                try {
                    participantCount = Integer.parseInt(row.get(31).toString());
                } catch (NumberFormatException e) {
                    participantCount = -1;
                }
                String animator = row.get(22).toString();
                String partyPlaceName = row.get(21).toString();

                Birthdays birthday = new Birthdays();

                birthday.setActive(active);
                //birthday.setIdBirthday(idBDay);
                birthday.setAnimator(animator);
                birthday.setBirthdayProgType(birthdayProgType);
                birthday.setChildBDayAge(childBDayAge);
                birthday.setDateTo(dateTo);
                birthday.setDateFrom(dateFrom);
                birthday.setChildFirstName(childFirstName);
                birthday.setChildLastName(childLastName);
                birthday.setDuration(duration);
                birthday.setIdBDayProgType(idBDayProgType);
                birthday.setIdPlace(idPlace);
                birthday.setIdContactResType(idContactResType);
                birthday.setParticipantCount(participantCount);
                birthday.setParentEmail(parentEmail);
                birthday.setParentFirstName(parentFirstName);
                birthday.setParentLastName(parentLastName);
                birthday.setIdLocation(idLocation);
                birthday.setIdPartyPlaceName(partyPlaceName);


                if (!dontSave) {

                    birthdaysRepository.save(birthday);

                }

            }


        }
    }

    @Override
    public void mapSheetCopyRU(String sheetId, String sheetName) throws IOException {
        List<List<Object>> sheetData = googleSheetsService.readSheetRange(sheetId, sheetName);

        for (List<Object> row : sheetData) {

            Integer active = Integer.parseInt(row.get(52).toString());
            Boolean dontSave = false;

            if (active == 1) {

                Integer idBDayProgType = Integer.parseInt(row.get(49).toString());
                Integer idPlace = Integer.parseInt(row.get(51).toString());
                Integer idLocation = Integer.parseInt(row.get(53).toString());
               /* Long idBDay;
                try {
                    idBDay = Long.parseLong(row.get(54).toString());
                } catch (NumberFormatException e) {
                    idBDay = 0L;
                    dontSave = true;
                }*/

                String input = row.get(55).toString();
                input = input.replace('−', '-');

                Integer idContactResType;
                try {
                    idContactResType = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    idContactResType = -1;
                }
                LocalDateTime dateFrom;

                try {
                    dateFrom = LocalDateTime.parse(row.get(56).toString(), formatter);

                } catch (DateTimeParseException e) {
                    dateFrom = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }

                LocalDateTime dateTo;

                try {
                    dateTo = LocalDateTime.parse(row.get(57).toString(), formatter);
                } catch (DateTimeException e) {

                    dateTo = LocalDateTime.of(1999, 1, 1, 1, 1, 1);
                }


                String duration = row.get(8).toString();
                String birthdayProgType = row.get(6).toString();
                String parentEmail = row.get(35).toString();
                String parentFirstName = row.get(37).toString();
                String parentLastName = row.get(38).toString();
                String childFirstName = row.get(29).toString();
                String childLastName = row.get(30).toString();
                Integer childBDayAge;
                try {
                    childBDayAge = Integer.parseInt(row.get(32).toString());
                } catch (NumberFormatException e) {
                    childBDayAge = -1;
                }
                Integer participantCount;
                try {
                    participantCount = Integer.parseInt(row.get(31).toString());
                } catch (NumberFormatException e) {
                    participantCount = -1;
                }
                String animator = row.get(22).toString();
                String partyPlaceName = row.get(21).toString();

                Birthdays birthday = new Birthdays();

                birthday.setActive(active);
                birthday.setAnimator(animator);
                birthday.setBirthdayProgType(birthdayProgType);
                birthday.setChildBDayAge(childBDayAge);
                birthday.setDateTo(dateTo);
                birthday.setDateFrom(dateFrom);
                birthday.setChildFirstName(childFirstName);
                birthday.setChildLastName(childLastName);
                birthday.setDuration(duration);
                birthday.setIdBDayProgType(idBDayProgType);
                birthday.setIdPlace(idPlace);
                birthday.setIdContactResType(idContactResType);
                birthday.setParticipantCount(participantCount);
                birthday.setParentEmail(parentEmail);
                birthday.setParentFirstName(parentFirstName);
                birthday.setParentLastName(parentLastName);
                birthday.setIdLocation(idLocation);
                birthday.setIdPartyPlaceName(partyPlaceName);


                if (!dontSave) {

                    birthdaysRepository.save(birthday);

                }

            }

        }
    }

    @Override
    public void mapSheetImportSaveToDB(String sheetId, String sheetName, Integer idLocation) throws IOException {
        List<List<Object>> sheet = googleSheetsService.readSheetRangeFrom(sheetId, sheetName, "A3:V90000");

        for (List<Object> row : sheet) {
            Boolean dontSave = false;
            Boolean error = false;
            ErrorLog errorToDB = new ErrorLog();
            errorToDB.setReadData(row.toString());
            errorToDB.setSheet(sheetName);

            StringBuilder errorField = new StringBuilder();
            StringBuilder errorMessage = new StringBuilder();

            String parentEmail = getStringValue(row, 16, "parentEmail", errorField, errorMessage);

            if (parentEmail == null || parentEmail.isBlank()) {
                dontSave = true;
            } else {
                LocalDate date = getDateValue(row, 0, "date", errorField, errorMessage);
                LocalTime startTime = getTimeValue(row, 1, "startTime", errorField, errorMessage);
                LocalTime endTime = getTimeValue(row, 2, "endTime", errorField, errorMessage);

                String birthdayProgType = getStringValue(row, 3, "birthdayProgType", errorField, errorMessage);
                String birthdayPartyType = getStringValue(row, 4, "birthdayPartyType", errorField, errorMessage);
                String extraProgram = getStringValue(row, 5, "extraProgram", errorField, errorMessage);
                String extraProgramSubType = getStringValue(row, 6, "extraProgramSubType", errorField, errorMessage);
                String partyPlaceName = getStringValue(row, 7, "partyPlaceName", errorField, errorMessage);

                String animator = getStringValue(row, 8, "animator", errorField, errorMessage);
                String extraAnimator = getStringValue(row, 9, "extraAnimator", errorField, errorMessage);
                String childFirstName = getStringValue(row, 10, "childFirstName", errorField, errorMessage);
                String childLastName = getStringValue(row, 11, "childLastName", errorField, errorMessage);

                Integer participantCount = getIntegerValue(row, 12, "participantCount", errorField, errorMessage);
                Integer childBDayAge = getIntegerValue(row, 14, "childBDayAge", errorField, errorMessage);

                String phone = getStringValue(row, 17, "phone", errorField, errorMessage);
                String parentName = getStringValue(row, 18, "parentName", errorField, errorMessage);
                String parentLastName = getStringValue(row, 19, "parentLastName", errorField, errorMessage);
                String contactReservationType = getStringValue(row, 20, "contactReservationType", errorField, errorMessage);


                Integer idExtraProgram = extraProgram != null ? getIdExtraProgram(extraProgram) : null;
                Integer idExtraSubType = extraProgramSubType != null ? getIdExtraProgramSubType(extraProgramSubType) : null;
                Integer idPlace = partyPlaceName != null ? getIdPlace(partyPlaceName) : null;
                Integer idContactResType = contactReservationType != null ? getIdContactResType(contactReservationType) : null;
                Integer idBDayProgType = birthdayProgType != null ? getIdBDayProgType(birthdayProgType) : null;
                Integer idBirthdayPartyType = birthdayPartyType != null ? getIdBirthdayPartyType(birthdayPartyType) : null;



                LocalDateTime dateFrom = LocalDateTime.of(date, startTime);
                LocalDateTime dateTo = LocalDateTime.of(date, endTime);

                Birthdays birthday = setBirthdaysAndActive1NEW(idLocation, participantCount, parentEmail,
                        idPlace, parentName, parentLastName, idContactResType, childFirstName, childLastName, birthdayPartyType, birthdayProgType, childBDayAge,
                        dateFrom, dateTo, animator, extraAnimator, idBirthdayPartyType, partyPlaceName, idBDayProgType, idExtraProgram, idExtraSubType, phone);

                if (errorField.length() > 0) {
                    errorToDB.setDateFrom(dateFrom);
                    errorToDB.setDateTo(dateTo);
                    errorToDB.setParentEmail(parentEmail);
                    errorToDB.setErrorField(errorField.toString());
                    errorToDB.setErrorMessage(errorMessage.toString());
                    // od komentiri za error log
                    // errorLogRepository.save(errorToDB);
                }

                try {
                    if (birthdaysRepository.findByParentEmailAndDateFromAndChildFirstNameAndIdLocation(parentEmail, dateFrom, childFirstName, idLocation).isEmpty()) {
                        birthdaysRepository.save(birthday);
                    }
                } catch (Exception e) {
                    logger.info("error" + e.getMessage());
                }
            }
        }
    }

    // Helper methods for parsing values
    private String getStringValue(List<Object> row, int index, String fieldName, StringBuilder errorField, StringBuilder errorMessage) {
        try {
            return row.get(index).toString();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            errorField.append(fieldName).append(" ");
            errorMessage.append(e.getMessage()).append("; ");
            return null;
        }
    }

    private Integer getIntegerValue(List<Object> row, int index, String fieldName, StringBuilder errorField, StringBuilder errorMessage) {
        try {
            return Integer.parseInt(row.get(index).toString());
        } catch (IndexOutOfBoundsException | NullPointerException | NumberFormatException e) {
            errorField.append(fieldName).append(" ");
            errorMessage.append(e.getMessage()).append("; ");
            return 0;
        }
    }

    private LocalDate getDateValue(List<Object> row, int index, String fieldName, StringBuilder errorField, StringBuilder errorMessage) {
        try {
            return LocalDate.parse(row.get(index).toString(), formatterDMYOnly);
        } catch (IndexOutOfBoundsException | NullPointerException | DateTimeParseException e) {
            errorField.append(fieldName).append(" ");
            errorMessage.append(e.getMessage()).append("; ");
            return LocalDate.of(1999, 1, 1);
        }
    }

    private LocalTime getTimeValue(List<Object> row, int index, String fieldName, StringBuilder errorField, StringBuilder errorMessage) {
        try {
            return LocalTime.parse(row.get(index).toString(), formatterHMSOnly);
        } catch (IndexOutOfBoundsException | NullPointerException | DateTimeParseException e) {
            try {
                return LocalTime.parse(row.get(index).toString(), formatterHMOnly);
            } catch (IndexOutOfBoundsException | NullPointerException | DateTimeParseException f) {
                errorField.append(fieldName).append(" ");
                errorMessage.append(f.getMessage()).append("; ");
                return LocalTime.of(0, 0, 0);
            }
        }
    }


    @Override
    public List<Birthdays> mapSheetImportSaveToDBFromDate(String sheetId, String sheetName, Integer idLocation, String from) throws IOException {

        List<List<Object>> sheet = googleSheetsService.readSheetRangeFrom(sheetId, sheetName, "A3:V8000");
        List<Birthdays> resultList;

        resultList = new ArrayList<>();

        for (List<Object> row : sheet) {

            Boolean dontSave = false;
            Boolean error = false;
            ErrorLog errorToDB = new ErrorLog();
            errorToDB.setReadData(row.toString());
            errorToDB.setSheet(sheetName);

            String errorField = null;
            String errorMessage = null;

            LocalDate date;
            try {
                date = LocalDate.parse(row.get(0).toString(), formatterDMYOnly);

            } catch (DateTimeParseException e) {
                date = LocalDate.of(1999, 1, 1);

                errorField += "date ";
                error = true;
                errorMessage += e.getMessage();

            }
            LocalTime startTime;
            try {
                startTime = LocalTime.parse(row.get(1).toString(), formatterHMSOnly);

            } catch (DateTimeParseException | IndexOutOfBoundsException e) {
                try {
                    startTime = LocalTime.parse(row.get(1).toString(), formatterHMOnly);
                } catch (DateTimeParseException | IndexOutOfBoundsException f){
                    startTime = LocalTime.of(0, 0, 0);
                    errorField += "startTime ";
                    errorMessage += f.getMessage();
                    error = true;

                }
            }

            LocalTime endTime;
            try {
                endTime = LocalTime.parse(row.get(2).toString(), formatterHMSOnly);
            } catch (DateTimeParseException | IndexOutOfBoundsException e) {
                try {
                    endTime = LocalTime.parse(row.get(2).toString(), formatterHMOnly);
                } catch (DateTimeParseException | IndexOutOfBoundsException f) {
                    endTime = LocalTime.of(0, 0, 0);
                    errorField += "endTime ";
                    errorMessage += f.getMessage();
                    error = true;
                }

            }

            String birthdayProgType;
            try {
                birthdayProgType = row.get(3).toString();}
            catch (IndexOutOfBoundsException e ) {
                birthdayProgType = null;
                errorField += "birthdayProgType ";
                errorMessage += e.getMessage();
                error = true;

            }
            String birthdayPartyType;
            try {
                birthdayPartyType = row.get(4).toString();}
            catch (IndexOutOfBoundsException e) {
                birthdayPartyType = null;
                errorField += "birthdayPartyType ";
                error = true;
                errorMessage += e.getMessage();
            }

            String duration;
            String partyPlaceName;
            String animator = null;
            String extraAnimator = null ;
            String childFirstName = null;
            String childLastName = null;

            try {
                duration = row.get(5).toString();
            } catch (IndexOutOfBoundsException e) {
                duration = null;
                errorField += "duration ";
                error = true;
                errorMessage += e.getMessage();
            }

            try {
                partyPlaceName = row.get(6).toString();
            } catch (IndexOutOfBoundsException e) {
                partyPlaceName = null;
                errorField += "partyPlaceName ";
                error = true;
                errorMessage += e.getMessage();

            }
            try {
                animator = row.get(7).toString();
                extraAnimator = row.get(8).toString();
                childFirstName = row.get(9).toString();
                childLastName = row.get(10).toString();
            }
            catch (IndexOutOfBoundsException e ) {
                dontSave = true;
                errorField += "animator childFirstName childLastName ";
                error = true;
                errorMessage += e.getMessage();
            }

            Integer participantCount;
            try {
                participantCount = Integer.parseInt(row.get(11).toString());
            } catch (NumberFormatException | IndexOutOfBoundsException e ) {
                participantCount = 0;
                errorField += "participantCount ";
                error = true;
                errorMessage += e.getMessage();

            }
            Integer childBDayAge;
            if (row.size() > 13) {
                try {
                    childBDayAge = Integer.parseInt(row.get(13).toString());
                } catch (NumberFormatException e) {
                    childBDayAge = 0;
                    errorField += "childBdayAge ";
                    error = true;
                    errorMessage += e.getMessage();
                }
            } else {
                childBDayAge = 0;
            }


            String parentEmail;
            String parentName;
            String parentLastName;
            String contactReservationType;
            if (row.size() > 19) {
                parentEmail = row.get(15).toString();
                parentName = row.get(17).toString();
                parentLastName = row.get(18).toString();
                contactReservationType = row.get(19).toString();
            } else {
                parentEmail = "";
                parentName = "";
                parentLastName = "";
                contactReservationType = "";
                dontSave = true;
            }
            Integer idPlace = null;
            Integer idContactResType = null;
            Integer idBDayProgType = null;
            Integer idBirthdayPartyType = null;

            if (partyPlaceName != null && contactReservationType != null && birthdayPartyType != null && birthdayProgType != null ) {

                idPlace = getIdPlace(partyPlaceName);
                idContactResType = getIdContactResType(contactReservationType);
                idBDayProgType = getIdBDayProgType(birthdayProgType);
                idBirthdayPartyType = getIdBirthdayPartyType(birthdayPartyType);
            }

            LocalDateTime dateFrom = LocalDateTime.of(date, startTime);
            LocalDateTime dateTo = LocalDateTime.of(date, endTime);
            Integer locationId = idLocation;

            if (date.isAfter(LocalDate.parse(from, formatterString))) {

                if (parentEmail == null || parentEmail.isBlank()) {
                    dontSave = true;
                } else {


                    Birthdays birthday = setBirthdaysAndActive1(idLocation, participantCount, parentEmail, duration, idPlace,
                            parentName, parentLastName, idContactResType, childFirstName, childLastName, birthdayPartyType,
                            birthdayProgType, childBDayAge, dateFrom, dateTo, animator, extraAnimator, idBirthdayPartyType,
                            partyPlaceName, idBDayProgType);


                    if(error = true){
                        errorToDB.setErrorField(errorField);
                        errorToDB.setErrorMessage(errorMessage);
                        errorLogRepository.save(errorToDB);
                    }

                    resultList.add(birthday);
                    birthdaysRepository.save(birthday);
                }
            }
        }
        return resultList;
    }

    @Override
    public void mapAndSaveUpcomingBirthdays(String sheetId, String sheetName, Integer idLocation) throws IOException {
        birthdaysRepository.deleteUpcomingBdays(idLocation);
        logger.info("idlocation: " + idLocation.toString());
        List<List<Object>> sheetData = googleSheetsService.readSheetRangeFrom(sheetId,sheetName,"B3:AP12000");
                for(List<Object> row : sheetData) {
                    try {
                        logger.info("rowData: " + row.toString());
                        LocalDate date = LocalDate.parse(row.get(0).toString(), formatterDMYOnly);
                        if (date.isAfter(LocalDate.now().minusDays(1))) {
                            String parentEmail;
                            try {
                                parentEmail = row.get(26).toString();
                                logger.info("email: " + parentEmail);
                            } catch (NullPointerException | StringIndexOutOfBoundsException e) {
                                parentEmail = null;
                            }

                            if (parentEmail.isEmpty()) {

                            } else {
                                //Nared ERROR handling za vse še !!!

                                LocalTime startTime;
                                LocalTime endTime;
                                try {
                                    startTime = LocalTime.parse(row.get(2).toString(), formatterHMOnly);
                                } catch (DateTimeParseException | NullPointerException e) {
                                    try {
                                        startTime = LocalTime.parse(row.get(2).toString(), formatterHMSOnly);
                                    } catch (DateTimeParseException | NullPointerException f) {
                                        startTime = LocalTime.of(23, 59, 59);
                                    }
                                }

                                try {
                                    endTime = LocalTime.parse(row.get(3).toString(), formatterHMOnly);
                                } catch (DateTimeParseException | NullPointerException e2) {
                                    try {
                                        endTime = LocalTime.parse(row.get(3).toString(), formatterHMSOnly);
                                    } catch (DateTimeParseException | NullPointerException f2) {
                                        endTime = LocalTime.of(23, 59, 59);
                                    }
                                }
                                LocalDateTime dateFrom = LocalDateTime.of(date, startTime);
                                LocalDateTime dateTo = LocalDateTime.of(date, endTime);

                                Integer idBirthDayProgType;
                                String birthDayProgType;

                                String birthdayPartyType;
                                Integer idBirthdayPartyType;

                                String duration;
                                String partyPlace;
                                Integer idPlace = null;
                                String animator;
                                String extraAnimator;
                                String parentName;
                                String parentLastName;
                                String childFirstName;
                                String childLastName;

                                Integer participantCount;
                                Integer childBDayAge;

                                try {
                                    birthDayProgType = row.get(4).toString();
                                    idBirthDayProgType = getIdBDayProgType(birthDayProgType);
                                } catch (NullPointerException b) {
                                    birthDayProgType = null;
                                    idBirthDayProgType=null;
                                }

                                try {
                                    birthdayPartyType = row.get(5).toString();
                                    idBirthdayPartyType = getIdBDayProgType(birthdayPartyType);
                                } catch (NullPointerException b) {
                                    birthdayPartyType = null;
                                    idBirthdayPartyType=null;
                                }

                                String extraProgram = null;

                                try {
                                    extraProgram = row.get(6).toString();
                                }catch (NullPointerException e ) {

                                }

                                Integer idExtraProgram = null;
                                if(extraProgram != null) {
                                    idExtraProgram = getIdExtraProgram(extraProgram);
                                }

                                String extraProgramSubType = null;
                                Integer idExtraProgramSubType = null;

                                try {
                                    extraProgramSubType = row.get(7).toString();
                                }catch (NullPointerException e) {

                                }

                                if (extraProgramSubType != null) {
                                    idExtraProgramSubType = getIdExtraProgramSubType(extraProgramSubType);

                                }

                                try {
                                    partyPlace = row.get(10).toString();
                                } catch (NullPointerException b) {
                                    partyPlace = null;
                                }
                                if(partyPlace != null) {
                                    idPlace = getIdPlace(partyPlace);

                                }
                                try {
                                    animator = row.get(11).toString();
                                } catch (NullPointerException b) {
                                    animator = null;
                                }
                                try {
                                    extraAnimator = row.get(13).toString();
                                } catch (NullPointerException b) {
                                    extraAnimator = null;
                                }
                                try {
                                    parentName = row.get(28).toString();
                                    parentLastName = row.get(29).toString();
                                } catch (NullPointerException b) {
                                    parentName = null;
                                    parentLastName = null;
                                }
                                try {
                                    childFirstName = row.get(20).toString();
                                    childLastName = row.get(21).toString();
                                } catch (NullPointerException b) {
                                    childFirstName = null;
                                    childLastName = null;
                                }
                                try {
                                    participantCount = Integer.parseInt(row.get(22).toString());
                                } catch (NumberFormatException | NullPointerException b) {
                                    participantCount = null;
                                }
                                try {
                                    childBDayAge = Integer.parseInt(row.get(23).toString());
                                } catch (NullPointerException | NumberFormatException b) {
                                    childBDayAge = null;
                                }

                                String phone = null;

                                try {
                                    phone = row.get(27).toString();
                                }catch (NullPointerException b) {

                                }
                                String inviteComments = null;
                                try {
                                    inviteComments = row.get(8).toString();
                                }catch (NullPointerException e) {

                                }
                                Integer minAge =null;
                                Integer maxAge = null;

                                try {
                                    minAge = Integer.valueOf(row.get(24).toString());
                                }catch (NullPointerException | NumberFormatException e ) {

                                }

                                try {
                                    maxAge = Integer.valueOf(row.get(25).toString());
                                }catch (NullPointerException | NumberFormatException e) {

                                }

                                Birthdays birthday = new Birthdays();

                                birthday.setParticipantCount(participantCount);
                                birthday.setParentEmail(parentEmail);
                                birthday.setIdExtraProgram(idExtraProgram);
                                birthday.setIdExtraProgramSubType(idExtraProgramSubType);
                                birthday.setIdLocation(idLocation);
                                birthday.setIdPlace(idPlace);
                                birthday.setParentFirstName(parentName);
                                birthday.setParentLastName(parentLastName);
                                birthday.setChildFirstName(childFirstName);
                                birthday.setChildLastName(childLastName);
                                birthday.setBirthdayPartyType(birthdayPartyType);
                                birthday.setBirthdayProgType(birthDayProgType);
                                birthday.setChildBDayAge(childBDayAge);
                                birthday.setDateFrom(dateFrom);
                                birthday.setDateTo(dateTo);
                                birthday.setAnimator(animator);
                                birthday.setDodatniAnimator(extraAnimator);
                                birthday.setIdBDayPartyType(idBirthdayPartyType);
                                birthday.setIdPartyPlaceName(partyPlace);
                                birthday.setIdBDayPartyType(idBirthdayPartyType);
                                birthday.setIdBDayProgType(idBirthDayProgType);
                                birthday.setUpcoming(1);
                                birthday.setPhone(phone);
                                birthday.setInviteComments(inviteComments);
                                birthday.setMaxAge(maxAge);
                                birthday.setMinAge(minAge);

                                birthdaysRepository.save(birthday);
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                        logger.info("indexOutofbounds");
                        break;
                    }
                }

    }



    @Override
    public void getDatesAndTimeForBirthdays(String sheetId, String sheetName) throws IOException {

        List<List<Object>> sheetData = googleSheetsService.readSheetRangeFrom(sheetId,sheetName,"A3:AN1200");

        HashMap<String, List<String>> dateTime = new HashMap<>();

        sheetData.forEach(row -> {
            LocalDate date = LocalDate.parse(row.get(1).toString(), formatterDMYOnly);
            String startTime = row.get(3).toString();

            if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusMonths(1)) && row.get(26).toString().isEmpty()) {
                dateTime.computeIfAbsent(date.toString(), k -> new ArrayList<>()).add(startTime);
            }
        });


        logger.info(dateTime.toString());
        AtomicReference<Integer> i = new AtomicReference<>(2);
        dateTime.forEach((date,time) -> {
            try {
                i.getAndSet(i.get() + 1);
                googleSheetsService.writeToSheet("1uYbtQ7tmswedrL4cHRsS956Cz_RDpvQSgjNpelJ3NYQ","A" + i + ":B" + i, date + " " + time);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private static Birthdays setBirthdaysAndActive1(Integer idLocation, Integer participantCount, String parentEmail,
                                          String duration, Integer idPlace, String parentName, String parentLastName,
                                          Integer idContactResType, String childFirstName, String childLastName,
                                          String birthdayPartyType, String birthdayProgType, Integer childBDayAge,
                                          LocalDateTime dateFrom, LocalDateTime dateTo, String animator, String extraAnimator,
                                          Integer idBirthdayPartyType, String partyPlaceName, Integer idBDayProgType) {
        Birthdays birthday = new Birthdays();

        birthday.setParticipantCount(participantCount);
        birthday.setParentEmail(parentEmail);
        birthday.setIdLocation(idLocation);
        birthday.setIdPlace(idPlace);
        birthday.setDuration(duration);
        birthday.setParentFirstName(parentName);
        birthday.setParentLastName(parentLastName);
        birthday.setActive(1);
        birthday.setIdContactResType(idContactResType);
        birthday.setChildFirstName(childFirstName);
        birthday.setChildLastName(childLastName);
        birthday.setBirthdayPartyType(birthdayPartyType);
        birthday.setBirthdayProgType(birthdayProgType);
        birthday.setChildBDayAge(childBDayAge);
        birthday.setDateFrom(dateFrom);
        birthday.setDateTo(dateTo);
        birthday.setAnimator(animator);
        birthday.setDodatniAnimator(extraAnimator);
        birthday.setIdBDayPartyType(idBirthdayPartyType);
        birthday.setIdPartyPlaceName(partyPlaceName);
        birthday.setIdBDayPartyType(idBirthdayPartyType);
        birthday.setIdBDayProgType(idBDayProgType);
        return birthday;
    }

    private static Birthdays setBirthdaysAndActive1NEW(Integer idLocation, Integer participantCount, String parentEmail,
                                                     Integer idPlace, String parentName, String parentLastName,
                                                    Integer idContactResType, String childFirstName, String childLastName,
                                                    String birthdayPartyType, String birthdayProgType, Integer childBDayAge,
                                                    LocalDateTime dateFrom, LocalDateTime dateTo, String animator, String extraAnimator,
                                                    Integer idBirthdayPartyType, String partyPlaceName, Integer idBDayProgType, Integer idExtraProgram,
                                                       Integer idExtraSubType, String phone) {
        Birthdays birthday = new Birthdays();

        birthday.setParticipantCount(participantCount);
        birthday.setParentEmail(parentEmail);
        birthday.setIdLocation(idLocation);
        birthday.setIdPlace(idPlace);
        birthday.setParentFirstName(parentName);
        birthday.setParentLastName(parentLastName);
        birthday.setActive(1);
        birthday.setIdContactResType(idContactResType);
        birthday.setChildFirstName(childFirstName);
        birthday.setChildLastName(childLastName);
        birthday.setBirthdayPartyType(birthdayPartyType);
        birthday.setBirthdayProgType(birthdayProgType);
        birthday.setChildBDayAge(childBDayAge);
        birthday.setDateFrom(dateFrom);
        birthday.setDateTo(dateTo);
        birthday.setAnimator(animator);
        birthday.setDodatniAnimator(extraAnimator);
        birthday.setIdBDayPartyType(idBirthdayPartyType);
        birthday.setIdPartyPlaceName(partyPlaceName);
        birthday.setIdBDayPartyType(idBirthdayPartyType);
        birthday.setIdBDayProgType(idBDayProgType);
        birthday.setIdExtraProgram(idExtraProgram);
        birthday.setIdExtraProgramSubType(idExtraSubType);
        birthday.setPhone(phone);


        return birthday;
    }

    private static Integer getIdContactResType(String contactReservationType) {
        Integer idContactResType;
        idContactResType = switch (contactReservationType.toUpperCase().trim()) {
            case "SPLET" -> -1;
            case "RECEPCIJA" -> 1;
            case "TELEFON" -> 2;
            case "EMAIL" -> 3;
            default -> 0;
        };
        return idContactResType;
    }

    public  Integer getIdBDayProgType(String birthdayProgType) {
        Integer idBDayProgType;
        idBDayProgType = switch (birthdayProgType.toLowerCase().trim()) {
            case "jump", "skakalna zabava" -> 1;
            case "super jump", "super fun zabava" -> 2;
            case "mini woop!", "mini woop" -> 3;
            case "tiktok jump" -> 4;
            case "lov na zaklad tematski" -> 5;
            case "lov na zaklad črke" -> 6;
            case "lov na zaklad simboli" -> 7;
            case "karting & jump kombo" -> 8;
            case "fun walls", "plezalna zabava" -> 9;
            case "kombo po meri" -> 10;
            case "mini fiesta" -> 11;
            case "maxi fiesta" -> 12;
            case "glow golf", "glow golf žur" -> 13;
            case "karting & glow golf kombo" -> 14;
            case "karting & bowling kombo" -> 15;
            case "laser tag" -> 16;
            case "bowling" -> 17;
            case "bowling odrasli" -> 18;
            case "cosmic bowling" -> 19;
            case "er" -> 20;
            case "escape room" -> 20;
            case "vr zabava" -> 21;
            case "bowling & laser tag kombo" -> 22;
            case "bowling & vr kombo" -> 23;
            case "izzivi","zabava woop! izzivi" -> 24;
            case "kombo vr & lt" -> 25;
            case "kombo glow golf + jump" -> 26;
            case "kombo fw + tp" -> 27;
            case "mega fun" -> 28;
            case "glow golf in vr žur", "glow golf in vr zur" -> 29;
            case "maksi karting žur" -> 30;
            case "ultra woop! izzivi zabava" -> 31;
            case "mega kombo zabava" -> 32;
            case "mega fun zabava" -> 33;
            case "cosmic bowling + 1vr" -> 34;
            case "bowling + vr upsell" -> 35;
            case "laser tag + vr upsell" -> 36;
            case "er + vr upsell" -> 37;
            case "zabava na trampolinih" -> 38;
            case "karting zabava" -> 39;
            case "zabava na woop! izzivih" -> 41;
            case "escape room zabava" -> 42;
            case "glow golf zabava" -> 43;
            case "bowling zabava" -> 44;
            case "laser tag zabava" -> 45;
            default -> 0;

        };
        return idBDayProgType;
    }


    public  Integer getIdBirthdayPartyType(String birthdayPartyType) {
        Integer idBirthdayPartyType;
        idBirthdayPartyType = switch (birthdayPartyType.toUpperCase().trim()) {
            case "LNZ ČAROBNI GOZD" -> 1;
            case "LNZ PODVODNI SVET" -> 2;
            case "LNZ PIRATI" -> 3;
            case "LNZ ČRKE RUMENA" -> 4;
            case "LNZ ČRKE MODRA" -> 5;
            case "LNZ ČRKE ROZA" -> 6;
            case "LNZ SIMBOLI RUMENA" -> 7;
            case "LNZ SIMBOLI MODRA" -> 8;
            case "LNZ SIMBOLI ROZA" -> 9;
            case "LT2 + VR" -> 10;
            case "VR 2 + LT" -> 11;
            case "WOOPATLON" -> 12;
            case "LNZ NINJA TRENING" -> 13;
            case "LNZ NINJE" -> 14;
            case "LNZ SKRITI AGENTI" -> 15;
            case "LNZ VESOLJCI" -> 16;
            case "SUPER" -> 17;
            case "MEGA" -> 18;
            case "VIP","ULTRA" -> 19;
            default -> 0;
        };
        return idBirthdayPartyType;
    }

    private static Integer getIdPlace(String partyPlaceName) {
        Integer idPlace;
        idPlace  = switch (partyPlaceName.toUpperCase()) {
            case "SVETLO MODRA" -> 3;
            case "TEMNO MODRA" -> 4;
            case "ROZA" -> 5;
            case "VIJOLIČNA" -> 6;
            case "RUMENA" -> 8;
            case "BELA" -> 9;
            case "MINIWOOP" -> 23;
            case "DPLACE" -> 24;
            case "SILVERSTONE SOBA" -> 397;
            case "MONACO SOBA" -> 32;
            case "DAYTONA SOBA" -> 33;
            case "TERARI 1" -> 103;
            case "VIP 1" -> 104;
            case "ER 1" -> 105;
            case "TERARI 2" -> 106;
            case "VIP 2" -> 107;
            case "ER 2" -> 108;
            case "VR" -> 109;
            case "VIP 3" -> 110;
            case "LT" -> 111;
            case "LT 2" -> 112;
            case "VIP 4" -> 113;
            case "PARTY PROSTOR 1" -> 303;
            case "PARTY PROSTOR 2" -> 304;
            case "PARTY PROSTOR 3" -> 305;
            case "PARTY PROSTOR 4" -> 306;
            case "PARTY SOBA 1" -> 203;
            case "PARTY SOBA 2" -> 204;
            case "PARTY SOBA 3" -> 205;
            default -> 0;
        };
        return idPlace;
    }

    private static Integer getIdExtraProgram(String extraProgram){

        return switch (extraProgram.trim().toLowerCase()){
            case "lnz" -> 1;
            case "cosmic" -> 2;
            default -> 0;
        };
    }

    public static Integer getIdExtraProgramSubType(String extraProgramSubTypeName){
        return switch (extraProgramSubTypeName.trim().toLowerCase()) {
            case "pirati" -> 1;
            case "podvodni svet" -> 2;
            case "čarobni gozd" -> 3;
            case "ninje" -> 4;
            case "skriti agenti" -> 5;
            case "vesoljci" -> 6;
            case "črke - rumena proga" -> 7;
            case "črke - modra proga" -> 8;
            case "črke - roza proga" -> 9;
            case "simboli - rumena proga" -> 10;
            case "simboli - modra proga" -> 11;
            case "simboli - roza proga" -> 12;
            case "črke" -> 13;
            case "simboli" -> 14;
            default -> 0;
        };
    }




}
