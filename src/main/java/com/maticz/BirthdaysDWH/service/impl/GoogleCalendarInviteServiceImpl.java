package com.maticz.BirthdaysDWH.service.impl;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.maticz.BirthdaysDWH.service.GoogleCalendarInviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

@Service
public class GoogleCalendarInviteServiceImpl implements GoogleCalendarInviteService {

    private final Calendar service;

    @Autowired
    public GoogleCalendarInviteServiceImpl(Calendar service) {
        this.service = service;
    }

    @Override
    public String sendBirthdayInviteAndGetLink(String childName, LocalDateTime eventDateTime, String location, String guestEmail, Integer durationHours, Integer durationMinutes) throws Exception {
        Event event = new Event()
                .setSummary("RD " + childName)

                .setLocation(location)
                .setDescription("Vabilo " + childName);

        DateTime startDateTime = new DateTime(eventDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(ZoneId.systemDefault().toString());
        event.setStart(start);

        DateTime endDateTime = new DateTime(eventDateTime.plusHours(durationHours).plusMinutes(durationMinutes).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(ZoneId.systemDefault().toString());
        event.setEnd(end);

        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail(guestEmail)
        };
        event.setAttendees(List.of(attendees));

        String calendarId = "primary";
        event = service.events().insert(calendarId, event).setSendNotifications(true).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
        return event.getHtmlLink();
    }

    @Override
    public HashMap<String,Integer> checkGuestResponse(String eventId, String calendarId) throws IOException {
        Event event = service.events().get(calendarId, eventId).execute();

        List<EventAttendee> attendees = event.getAttendees();
        HashMap<String,Integer> emailAndResponse = new HashMap<String, Integer>();
        if (attendees != null) {
            for (EventAttendee attendee : attendees) {
                String email = attendee.getEmail();
                String responseStatus = attendee.getResponseStatus();

                emailAndResponse.put(email, emailResponseToInt(responseStatus));

                System.out.printf("Attendee %s response: %s%n", email, responseStatus);
            }
        } else {
            System.out.println("No attendees found for this event.");
        }
        return emailAndResponse;
    }

    @Override
    public String extractEventId(String urlString) {
        try {
            URL url = new URL(urlString);
            String query = url.getQuery();
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0 && pair.substring(0, idx).equals("eid")) {
                    return URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer emailResponseToInt(String response) {
        Integer responseInt = switch (response) {
            case "accepted" ->1;
            case "declined" ->0;
            case "needsAction" ->-1;

            default -> 2;
        }; return responseInt;
    }
}

