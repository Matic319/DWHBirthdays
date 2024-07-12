package com.maticz.BirthdaysDWH.config;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
/*
@Configuration
public class GoogleAuthorizationConfig {

    private final Environment env;

    public GoogleAuthorizationConfig(Environment env) {
        this.env = env;
    }

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final JsonFactory JSON_FACTORY = Utils.getDefaultJsonFactory();
    private static final String APPLICATION_NAME = "Google Sheets API Java Client";

    private GoogleClientSecrets getGoogleClientSecrets() throws IOException {
        String path = env.getProperty("credentials.file.path");
        System.out.println("Loading credentials from: " + path);
        InputStream in = GoogleAuthorizationConfig.class.getResourceAsStream(path);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + path);
        }
        return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    }

    private Credential authorize() throws IOException, GeneralSecurityException {
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                getGoogleClientSecrets(),
                SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(env.getProperty("tokens.directory.path"))))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    @Bean
    public Sheets sheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize();

        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}*/


@Configuration
public class GoogleAuthorizationConfig {

    private final Environment env;

    public GoogleAuthorizationConfig(Environment env) {
        this.env = env;
    }

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final JsonFactory JSON_FACTORY = Utils.getDefaultJsonFactory();
    private static final String APPLICATION_NAME = "Google Sheets API Java Client";

    private Credential authorize() throws IOException {
        // Load service account credentials from JSON file
        String path = env.getProperty("credentials.file.path");
        System.out.println("Loading credentials from: " + path);
        InputStream in = GoogleAuthorizationConfig.class.getResourceAsStream(path);
        if (in == null) {
            throw new IOException("Resource not found: " + path);
        }

        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(SCOPES);

        return credential;
    }

    @Bean
    public Sheets sheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize();

        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}
