package com.maticz.BirthdaysDWH.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
@Configuration
public class GoogleCalendarAuthorization {

    @Value("${credentials.file.path}")
    private String credentialsFilePath;

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final JsonFactory JSON_FACTORY = Utils.getDefaultJsonFactory();
    private static final String APPLICATION_NAME = "Birthday Invitation App";

    private Credential authorize() throws IOException {
        InputStream in = GoogleCalendarAuthorization.class.getResourceAsStream(credentialsFilePath);
        if (in == null) {
            throw new IOException("Resource not found: " + credentialsFilePath);
        }

        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(SCOPES)
                .createDelegated("matic.zigon@woop.fun");


        return credential;
    }

    @Bean
    public Calendar calendarService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize();

        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}