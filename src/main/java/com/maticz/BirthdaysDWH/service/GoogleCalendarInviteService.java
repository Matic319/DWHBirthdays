package com.maticz.BirthdaysDWH.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public interface GoogleCalendarInviteService {


    String sendBirthdayInviteAndGetLink(String childName, LocalDateTime eventDateTime, String location, String guestEmail, Integer durationHours, Integer durationMinutes) throws Exception;

    HashMap<String,Integer> checkGuestResponse(String eventId, String calendarId) throws IOException;

    String extractEventId(String urlString);


}