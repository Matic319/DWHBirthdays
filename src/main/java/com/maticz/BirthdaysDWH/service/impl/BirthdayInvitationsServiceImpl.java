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
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BirthdayInvitationsServiceImpl implements BirthdayInvitationsService {

    @Autowired
    GoogleSheetsServiceImpl googleSheetsService;

    @Autowired
    BirthdayInvitationsRepository birthdayInvitationsRepository;

    @Autowired
    GoogleCalendarInviteServiceImpl googleCalendarInviteService;

    @Autowired
    BirthdayServiceImpl birthdayService;

    private final Logger logger = LoggerFactory.getLogger(BirthdayInvitationsServiceImpl.class);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. M. yyyy HH:mm:ss");
    DateTimeFormatter formatterDMYOnly = DateTimeFormatter.ofPattern("d. M. yyyy");
    DateTimeFormatter formatterHM = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter formatterHMS = DateTimeFormatter.ofPattern("HH:mm:ss");


    @Override
    public void mapAndSaveToInvitations(String sheetId, String sheetName, Integer idLocation) throws IOException {
        List<List<Object>> sheetData = googleSheetsService.readSheetRangeFrom(sheetId, sheetName, "B3:AP1200");

        sheetData.forEach(row -> {
            LocalDate date = LocalDate.parse(row.get(0).toString(), formatterDMYOnly);
            if (date.isAfter(LocalDate.now())) {
                Boolean sendInvitation = Boolean.valueOf(row.get(18).toString());
                Boolean sendInviteToAnimator = Boolean.valueOf(row.get(14).toString());

                if (sendInvitation || sendInviteToAnimator) {
                    String emailParent = row.get(25) != null ? row.get(25).toString() : null;
                    if(emailParent != null){
                        if(!emailParent.contains("@")){
                            emailParent = null; // zapis v error log
                        }
                    }
                    String emailAnimator = row.get(11) != null  ? row.get(11).toString() : null;
                    if(emailAnimator != null){
                        if(!emailAnimator.contains("@")){
                            emailAnimator = null; // zapis v error log
                        }
                    }
                    String emailExtraAnimator = row.get(13) != null ? row.get(13).toString() : null;
                    if(emailExtraAnimator != null){
                        if(!emailExtraAnimator.contains("@")){
                            emailExtraAnimator = null; // zapiš
                        }
                    }

                    if (emailParent != null) {
                        LocalTime startTime;
                        LocalTime endTime;
                        try {
                            startTime = LocalTime.parse(row.get(2).toString(), formatterHM);
                        } catch (DateTimeParseException e) {
                            startTime = LocalTime.parse(row.get(2).toString(), formatterHMS);
                        }

                        try {
                            endTime = LocalTime.parse(row.get(3).toString(), formatterHM);
                        } catch (DateTimeParseException e) {
                            endTime = LocalTime.parse(row.get(3).toString(), formatterHMS);
                        } catch (Exception f) {
                            endTime = startTime.plusHours(3); // če je slučajno prazn, nastav default uro
                        }

                        String childName = row.get(19).toString();

                        Integer age;
                        try{
                            age = Integer.parseInt(row.get(22).toString());
                        }catch (NumberFormatException e){
                            age = 0;
                        }

                        String phone = row.get(26).toString();
                        LocalDateTime dateFrom = LocalDateTime.of(date, startTime);
                        String partyPlaceName = row.get(9).toString() != null ? row.get(9).toString() : null;
                        Integer durationHours = getDurationHours(startTime, endTime);
                        Integer durationMinutes = getDurationMinutes(startTime, endTime);
                        Integer minAge;
                        try {
                            minAge = Integer.parseInt(row.get(23).toString()); // mogoče pišejo s pomišljaji leta ?
                        }catch (NumberFormatException e) {
                            minAge = 0;
                        }
                        Integer maxAge;
                        try {
                            maxAge = Integer.parseInt(row.get(24).toString());
                        }catch (NumberFormatException e) {
                            maxAge = 0;
                        }
                        Integer participantCount;
                        try {
                            participantCount = Integer.parseInt(row.get(21).toString());
                        }catch (NumberFormatException e) {
                            participantCount = 0;
                        }
                        LocalDateTime dateTo = LocalDateTime.of(date,endTime);
                        String desserts = row.get(29).toString() != null ? row.get(29).toString() : null;
                        String food = row.get(30).toString() != null ? row.get(30).toString() : null;
                        String comments = row.get(8).toString() != null ? row.get(8).toString() : null;
                        String duration = row.get(7).toString() != null ? row.get(7).toString() : null;
                        String partyType = row.get(5).toString() != null ? row.get(6).toString() : null;
                        String attractionComments = row.get(4).toString() != null ? row.get(4).toString() : null;
                        String parentFirstName = row.get(27).toString() != null ? row.get(27).toString() : null;
                        String parentLastName = row.get(28).toString() != null ? row.get(28).toString() : null;
                        String description = setDescription(childName,
                                startTime,endTime,partyPlaceName,partyType,
                                duration,desserts,food,age,minAge,maxAge,participantCount,
                                comments,attractionComments);

                        if (birthdayInvitationsRepository.findByEmailAndDateFromAndIdLocationAndChildName(emailParent, dateFrom, idLocation, childName).isEmpty()) {
                           /* setBdayInvitationAndSave(idLocation,
                                    emailParent, dateFrom, childName, age, phone,
                                    emailAnimator, emailExtraAnimator, partyPlaceName,
                                    minAge, maxAge, participantCount, desserts, food,
                                    comments, duration, partyType, attractionComments,
                                    parentFirstName,parentLastName,dateTo);*/
                            if (sendInviteToAnimator) {
                                try {
                                    googleCalendarInviteService.birthdayInviteToCalendarAndDB(childName, dateFrom, partyPlaceName, emailAnimator,
                                            durationHours, durationMinutes, idLocation,description,emailParent,false);
                                    if (emailExtraAnimator != null) {
                                        googleCalendarInviteService.birthdayInviteToCalendarAndDB(childName, dateFrom, partyPlaceName,
                                                emailExtraAnimator, durationHours, durationMinutes, idLocation,description,emailParent,true);
                                    }
                                } catch (Exception e) {
                                    System.out.println("couldn't send" + e.getMessage());
                                }
                            }
                        } else {
                            if (emailAnimator != null &&
                                    birthdayInvitationsRepository.findByEmailAndDateFromAndChildNameAndIdLocationAndAnimatorEmail(
                                            emailParent, dateFrom, childName, idLocation, emailAnimator).isEmpty()) {
                                birthdayInvitationsRepository.updateValueForEmailAnimatorAndSetInviteSentToZero(
                                        emailParent, dateFrom, idLocation, childName, emailAnimator);
                                if (sendInviteToAnimator) {
                                    try {
                                        googleCalendarInviteService.birthdayInviteToCalendarAndDB(childName, dateFrom, partyPlaceName,
                                                emailAnimator, durationHours, durationMinutes, idLocation,description, emailParent,false);
                                    } catch (Exception e) {
                                        System.out.println("couldn't send" + e.getMessage());
                                    }
                                }

                            } else {
                                if (!checkIfInviteWasAlreadySent(emailParent, dateFrom, childName, false, idLocation)) {
                                    try {
                                        googleCalendarInviteService.birthdayInviteToCalendarAndDB(childName, dateFrom, partyPlaceName, emailAnimator,
                                                durationHours, durationMinutes, idLocation,description, emailParent,false);
                                    } catch (Exception e) {
                                        System.out.println("error birthdayInviteToCalendarAndDB : " + e.getMessage() );
                                    }
                                }else {
                                    String inviteId = birthdayInvitationsRepository.getInviteIdWhereThereIsNoResponse(emailAnimator,idLocation,dateFrom);
                                        if(inviteId != null){
                                            try {
                                                googleCalendarInviteService.getResponseAndSaveToDB(inviteId,"matic.zigon@woop.fun",false,emailAnimator); // uved spremenljivko za calendarID !!!!!
                                            } catch (IOException e) {
                                                System.out.println("error gerResponseAndSaveToDB:" + e.getMessage());
                                            }
                                        }
                                }
                            }

                            if (emailExtraAnimator != null && birthdayInvitationsRepository
                                    .findByEmailAndDateFromAndChildNameAndIdLocationAndExtraAnimatorEmail(
                                            emailParent, dateFrom, childName, idLocation, emailExtraAnimator
                                    ).isEmpty()) {
                                birthdayInvitationsRepository.updateValueForExtraAnimatorEmailAndSetInviteSentToZero(
                                        emailParent, dateFrom, idLocation, childName, emailExtraAnimator);
                                if (sendInviteToAnimator) {
                                    try {
                                        googleCalendarInviteService.birthdayInviteToCalendarAndDB(childName, dateFrom, partyPlaceName,
                                                emailExtraAnimator, durationHours, durationMinutes, idLocation,description,emailParent,false);
                                    } catch (Exception e) {
                                        System.out.println("couldn't send 2 " + e.getMessage());
                                    }
                                }
                            } else if (emailExtraAnimator != null && !checkIfInviteWasAlreadySent(emailParent, dateFrom, childName, true, idLocation)) {
                                try {
                                    googleCalendarInviteService.birthdayInviteToCalendarAndDB(childName, dateFrom, partyPlaceName,
                                            emailExtraAnimator, durationHours, durationMinutes, idLocation,description,emailParent,true);
                                } catch (Exception e) {
                                    System.out.println("error birthdayINviteTocalendarAndDB : " + e.getMessage() );
                                }
                            } else if (emailExtraAnimator != null && checkIfInviteWasAlreadySent(emailParent, dateFrom, childName, true, idLocation)){
                                String inviteId = birthdayInvitationsRepository.getInviteIdWhereThereIsNoResponse(emailAnimator,idLocation,dateFrom);
                                try {
                                    googleCalendarInviteService.getResponseAndSaveToDB(inviteId,"matic.zigon@woop.fun",true,emailExtraAnimator);
                                } catch (IOException e) {
                                    System.out.println("error gerReSponseAndSAveToDB:" + e.getMessage());
                                }
                            }
                        }

                    }
                }
            }
        });
    }

    @Override
    public void mapAndSaveEmailInvitationData(String sheetId, String sheetName, Integer idLocation) throws IOException {
        List<List<Object>> sheetData = googleSheetsService.readSheetRangeFrom(sheetId, sheetName, "B3:AQ1200");

        for (List<Object> row : sheetData) {
            LocalDate date = LocalDate.of(1999, 1, 1);
            try {
                date = LocalDate.parse(row.get(0).toString(), formatterDMYOnly);
            } catch (DateTimeParseException | NullPointerException e) {

            }
                if (date.isAfter(LocalDate.now())) {
                    String sendEmailInvite = row.get(19) != null ? row.get(19).toString() : null;
                    if (sendEmailInvite != null && sendEmailInvite.equalsIgnoreCase("poslano")) {
                        String birthdayProgType = row.get(4).toString();
                        if (!birthdayProgType.toLowerCase().contains("kombo")) {
                            String parentEmail = row.get(26).toString();
                            String childName = row.get(20).toString();
                            Integer participantCount = null;
                            try {
                                participantCount = Integer.parseInt(row.get(22).toString());
                            } catch (NumberFormatException ignored) {

                            }
                            Integer age = Integer.parseInt(row.get(23).toString());
                            Integer minAge = Integer.parseInt(row.get(24).toString());
                            Integer maxAge = Integer.parseInt(row.get(25).toString());

                            String partyType = row.get(5) != null ? row.get(5).toString() : null;
                            String phone = row.get(27).toString();

                            Integer idBirthdayProgType = birthdayService.getIdBDayProgType(birthdayProgType);
                            Integer idPartyType = partyType != null ? birthdayService.getIdBirthdayPartyType(partyType) : 0;

                            String commentsForParents = row.get(8) != null ? row.get(8).toString() : null;
                            String requiredAnimatorString = row.get(41) != null ? row.get(41).toString() : "";
                            Integer requiredAnimator = null;
                            if (requiredAnimatorString.equalsIgnoreCase("true")) {
                                requiredAnimator = 1;
                            } else {
                                requiredAnimator = 0;
                            }
                            LocalTime startTime;
                            LocalTime endTime;
                            try {
                                startTime = LocalTime.parse(row.get(2).toString(), formatterHM);
                            } catch (DateTimeParseException e) {
                                startTime = LocalTime.parse(row.get(2).toString(), formatterHMS);
                            }

                            try {
                                endTime = LocalTime.parse(row.get(3).toString(), formatterHM);
                            } catch (DateTimeParseException e) {
                                try {
                                    endTime = LocalTime.parse(row.get(3).toString(), formatterHMS);

                                }catch (DateTimeParseException f ) {
                                    endTime = startTime.plusHours(3);
                                }
                            }
                            String parentFirstName = row.get(28).toString();

                            LocalDateTime dateFrom = LocalDateTime.of(date, startTime);
                            LocalDateTime dateTo = LocalDateTime.of(date, endTime);
                            if (birthdayInvitationsRepository.findByEmailAndDateFromAndIdLocationAndChildName(parentEmail, dateFrom, idLocation, childName).isEmpty()) {
                                setBdayInvitationAndSave(idLocation, parentEmail, dateFrom, dateTo, childName, idBirthdayProgType, idPartyType, age,
                                        minAge, maxAge, commentsForParents, requiredAnimator,phone, participantCount, parentFirstName,
                                        birthdayProgType,partyType);
                            }

                        }

                    }

                }
        }
    }

    private void setBdayInvitationAndSave(Integer idLocation, String parentEmail, LocalDateTime dateFrom, LocalDateTime dateTo,
                                          String childName, Integer idBirthdayProgType, Integer idPartyType,
                                          Integer age, Integer minAge, Integer maxAge,
                                          String commentsForParents, Integer requiredAnimator, String phone,
                                          Integer participantCount, String parentFirstName, String birthdayProgramType,
                                          String partyType) {
        BirthdayInvitations bdInv = new BirthdayInvitations();

        bdInv.setEmailSent(0);
        bdInv.setIdLocation(idLocation);
        bdInv.setEmail(parentEmail);
        bdInv.setChildName(childName);
        bdInv.setIdBirthdayProgType(idBirthdayProgType);
        bdInv.setIdPartyType(idPartyType);
        bdInv.setDateFrom(dateFrom);
        bdInv.setDateTo(dateTo);
        bdInv.setAge(age);
        bdInv.setMinAge(minAge);
        bdInv.setMaxAge(maxAge);
        bdInv.setCommentsForParents(commentsForParents);
        bdInv.setRequiredAnimator(requiredAnimator);
        bdInv.setImportTimestamp(LocalDateTime.now());
        bdInv.setPhone(phone);
        bdInv.setParticipantCount(participantCount);
        bdInv.setParentFirstName(parentFirstName);
        bdInv.setProgramType(birthdayProgramType);
        bdInv.setPartyType(partyType);
        birthdayInvitationsRepository.save(bdInv);
    }

    private Boolean checkIfInviteWasAlreadySent(String parentEmail, LocalDateTime dateFrom, String childName, Boolean searchByExtraAnimator, Integer idLocation) {
        if (searchByExtraAnimator) {
            return birthdayInvitationsRepository.hasExtraAnimatorInviteBeenSent(parentEmail, dateFrom, idLocation, childName).isPresent();
        } else {
            return birthdayInvitationsRepository.hasAnimatorInviteBeenSent(parentEmail, dateFrom, idLocation, childName).isPresent();
        }
    }

    private Integer getDurationHours(LocalTime startTime, LocalTime endTime) {
        return Math.toIntExact(ChronoUnit.HOURS.between(startTime, endTime));
    }

    private Integer getDurationMinutes(LocalTime startTime, LocalTime endTime) {
        long totalMinutes = ChronoUnit.MINUTES.between(startTime, endTime);
        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        long remainingMinutes = totalMinutes - (hours * 60);
        return Math.toIntExact(remainingMinutes);
    }


    private String setDescription(String childName, LocalTime startTime, LocalTime endTime, String partyPlaceName, String partyType, String duration,
                                  String desserts, String food, Integer age, Integer minAge, Integer maxAge, Integer participantCount, String comments, String attractionComments) {
        return """
        Slavejenec: %s %d let
        Ura RD: %s - %s
        Soba: %s Program: %s %s
        Št otrok: %d
        Min starost: %d Max starost: %d
        Hrana: %s
        Sladice: %s
        %s
        %s
        """.formatted(childName, age, startTime, endTime, partyPlaceName, partyType, duration, participantCount, minAge, maxAge, food, desserts,
                comments,attractionComments);
    }
}