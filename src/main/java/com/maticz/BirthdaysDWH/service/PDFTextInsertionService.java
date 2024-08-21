package com.maticz.BirthdaysDWH.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PDFTextInsertionService {

    String locationAddress(Integer idLocation);

    void insertText(PDPageContentStream contentStream, String text, float xInches, float yInches,
                    float pageHeightInPoints, float fontSize, PDDocument document) throws IOException;

    float inchesToPoints(float inches);

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

    byte[] createPdfInMemoryBDayForm(String date, String starTime, String endTime, String partyType, String childName, String childSurname,
                                         String participantCount, String age, String phone, String partyPlace, String minAge, String maxAge, String parentName,
                                         String comments, String animator, String partySubProgram) throws IOException;


}
