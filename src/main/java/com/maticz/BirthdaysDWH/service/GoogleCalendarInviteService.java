package com.maticz.BirthdaysDWH.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public interface GoogleCalendarInviteService {



    HashMap<String,Integer> checkGuestResponse(String eventId, String calendarId) throws IOException;

    void birthdayInviteToCalendarAndDB(String childName, LocalDateTime eventDateTime, String location,
                                       String guestEmail, Integer durationHours, Integer durationMinutes,
                                       Integer idLocation, String description, String parentEmail, Boolean extraAnimator) throws Exception;

    String sendBirthdayInviteAndGetEventId(String childName, LocalDateTime eventDateTime, String location, String guestEmail, Integer durationHoursAnimator,
                                           Integer durationMinutesAnimator, String description, Integer idLocationForCalendarId) throws Exception;

    void getResponseAndSaveToDB(String eventId, String calendarId, Boolean extraAnimator, String animatorEmail) throws IOException;
}