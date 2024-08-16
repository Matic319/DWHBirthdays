package com.maticz.BirthdaysDWH.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PDFTextInsertionService {

    String locationAddress(Integer idLocation);

    void insertTextIntoPdfInvite(String outputFilePath, String age,
                                 String dateFrom, String time,
                                 String phone, String childName, Integer idLocation) throws IOException;

     void convertPdfToJpgAndSave(String inputFilePath, String outputFilePrefix, int dpi) throws IOException;

     byte[] createPdfInMemory(String age, String dateFrom, String time, String phone, String childName, Integer idLocation) throws IOException;

     String convertPhoneNumber(String phone);

      byte[] createAndConvertPdfToJpg(String age, String dateFrom, String time, String phone, String childName, Integer idLocation) throws IOException;

    String locationName(Integer idLocation);



    void insertTextIntoPdfFormForBDay(String outputFilePath, String date, String time, String partyType, String childName,
                                      String childSurname, String participantCount, String age, String phone, String partyPlace) throws IOException;

    byte[] createPdfInMemoryBDayForm(String date, String starTime,String endTime,  String partyType, String childName, String childSurname,
                                     String participantCount, String age, String phone, String partyPlace, String minAge, String maxAge, String parentName,
                                     String comments, String animator, String partySubProgram) throws IOException;
}
