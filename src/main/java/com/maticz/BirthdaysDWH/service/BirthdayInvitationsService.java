package com.maticz.BirthdaysDWH.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface BirthdayInvitationsService {

    void mapAndSaveToInvitations(String sheetId, String sheetName, Integer idLocation ) throws IOException;
}
