package com.maticz.BirthdaysDWH.service.impl;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.maticz.BirthdaysDWH.repository.BirthdayInvitationsRepository;
import com.maticz.BirthdaysDWH.service.GoogleCalendarInviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

@Service
public class GoogleCalendarInviteServiceImpl implements GoogleCalendarInviteService {

    @Autowired
    BirthdayInvitationsRepository birthdayInvitationsRepository;

    private final Calendar service;

    @Autowired
    public GoogleCalendarInviteServiceImpl(Calendar service) {
        this.service = service;
    }

    @Override
    public String sendBirthdayInviteAndGetEventId(String childName, LocalDateTime eventDateTime, String location, String guestEmail, Integer durationHours,
                                                  Integer durationMinutes, String description) throws Exception {
        Event event = new Event()
                .setSummary("RD " + childName)

                .setLocation(location)
                .setDescription(description);

        DateTime startDateTime = new DateTime(eventDateTime.minusMinutes(15).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()); // minus 15 minut zarad animatorja
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(ZoneId.systemDefault().toString());
        event.setStart(start);

        DateTime endDateTime = new DateTime(eventDateTime.plusHours(durationHours).plusMinutes(durationMinutes).plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
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

       return event.getId();
        //return event.getHtmlLink();
    }

    @Override
    public void getResponseAndSaveToDB(String eventId, String calendarId , Boolean extraAnimator, String animatorEmail) throws IOException {
       HashMap<String, Integer> emailAndResponse =  checkGuestResponse(eventId,calendarId);
       if(!extraAnimator){
           birthdayInvitationsRepository.updateAnimatorInviteResponse(emailAndResponse.get(animatorEmail), eventId);
       }else {
           birthdayInvitationsRepository.updateExtraAnimatorInviteResponse(emailAndResponse.get(animatorEmail), eventId);
       }
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
    public void birthdayInviteToCalendarAndDB(String childName, LocalDateTime eventDateTime, String location, String emailToSendInvitationTo, Integer durationHoursAnimator
            , Integer durationMinutesAnimator, Integer idLocation,String description, String parentEmail, Boolean extraAnimator) throws Exception {
        String eventId = sendBirthdayInviteAndGetEventId(childName, eventDateTime, location, emailToSendInvitationTo, durationHoursAnimator,durationMinutesAnimator,description);

        if(extraAnimator){
            birthdayInvitationsRepository.updateExtraAnimatorInviteSentToTrueAndAddEventId(parentEmail,eventDateTime,idLocation,childName,eventId);
        }else {
            birthdayInvitationsRepository.updateAnimatorInviteSentToTrueAndAddEventId(parentEmail,eventDateTime,idLocation,childName,eventId);
        }
    }

    private Integer emailResponseToInt(String response) {
        return switch (response) {
            case "accepted" ->1;
            case "declined" ->0;
            case "needsAction" ->-1;

            default -> 2;
        };
    }

}

