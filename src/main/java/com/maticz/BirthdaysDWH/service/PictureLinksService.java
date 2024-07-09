package com.maticz.BirthdaysDWH.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PictureLinksService {


    void mapSheetAndSaveToDB(String idSheet, String sheetName, Integer idLocation) throws IOException;

    void updatePictureLinkInAC(Integer idLocation) throws IOException;

    void updateEmailSentAndEmailOpened();

    void writeToSheetBasedOnQuerry(Integer idLocation , String sheetId) throws IOException;
}