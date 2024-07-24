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

    private final Logger logger = LoggerFactory.getLogger(BirthdayInvitationsServiceImpl.class);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. M. yyyy HH:mm:ss");
    DateTimeFormatter formatterDMYOnly = DateTimeFormatter.ofPattern("d. M. yyyy");
    DateTimeFormatter formatterHM = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter formatterHMS = DateTimeFormatter.ofPattern("HH:mm:ss");


    @Override
    public void mapAndSaveToInvitations(String sheetId, String sheetName, Integer idLocation) throws IOException {
        List<List<Object>> sheetData = googleSheetsService.readSheetRangeFrom(sheetId, sheetName, "B3:AN1200");

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
                        String partyType = row.get(6).toString() != null ? row.get(6).toString() : null;
                        String attractionComments = row.get(4).toString() != null ? row.get(4).toString() : null;
                        String parentFirstName = row.get(27).toString() != null ? row.get(27).toString() : null;
                        String parentLastName = row.get(28).toString() != null ? row.get(28).toString() : null;
                        String description = setDescription(childName,
                                startTime,endTime,partyPlaceName,partyType,
                                duration,desserts,food,age,minAge,maxAge,participantCount,
                                comments,attractionComments);

                        if (birthdayInvitationsRepository.findByEmailAndDateFromAndIdLocationAndChildName(emailParent, dateFrom, idLocation, childName).isEmpty()) {
                            setBdayInvitationAndSave(idLocation,
                                    emailParent, dateFrom, childName, age, phone,
                                    emailAnimator, emailExtraAnimator, partyPlaceName,
                                    minAge, maxAge, participantCount, desserts, food,
                                    comments, duration, partyType, attractionComments,
                                    parentFirstName,parentLastName,dateTo);
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

    private void setBdayInvitationAndSave(Integer idLocation, String email, LocalDateTime dateFrom, String childName,
                                          Integer age, String phone, String animatorEmail, String extraAnimatorEmail,
                                          String partyPlaceName, Integer minAge, Integer maxAge, Integer participantCount,
                                          String desserts, String food, String comments, String duration, String partyType,
                                          String attractionComments, String parentFirstName, String parentLastName,
                                          LocalDateTime dateTo) {
        BirthdayInvitations bdInv = new BirthdayInvitations();
        bdInv.setEmail(email);
        bdInv.setDateFrom(dateFrom);
        bdInv.setChildName(childName);
        bdInv.setAge(age);
        bdInv.setImportTimestamp(LocalDateTime.now());
        bdInv.setIdLocation(idLocation);
        bdInv.setPhone(phone);
        bdInv.setEmailSent(0);
        bdInv.setAnimatorInviteSent(0);
        bdInv.setExtraAnimatorInviteSent(0);
        bdInv.setAnimatorEmail(animatorEmail);
        bdInv.setExtraAnimatorEmail(extraAnimatorEmail);
        bdInv.setPartyPlaceName(partyPlaceName);
        bdInv.setMinAge(minAge);
        bdInv.setMaxAge(maxAge);
        bdInv.setParticipantCount(participantCount);
        bdInv.setDesserts(desserts);
        bdInv.setFood(food);
        bdInv.setComments(comments);
        bdInv.setDuration(duration);
        bdInv.setPartyType(partyType);
        bdInv.setAttractionComments(attractionComments);
        bdInv.setParentFirstName(parentFirstName);
        bdInv.setParentLastName(parentLastName);
        bdInv.setDateTo(dateTo);
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