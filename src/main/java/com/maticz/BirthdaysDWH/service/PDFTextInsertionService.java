package com.maticz.BirthdaysDWH.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PDFTextInsertionService {

    String locationAddress(Integer idLocation);

    void insertTextIntoPdf(String outputFilePath, String age,
                           String dateFrom, String time,
                           String phone, String childName, Integer idLocation) throws IOException;

     void convertPdfToJpgAndSave(String inputFilePath, String outputFilePrefix, int dpi) throws IOException;

     byte[] createPdfInMemory(String age, String dateFrom, String time, String phone, String childName, Integer idLocation) throws IOException;

     String convertPhoneNumber(String phone);

      byte[] createAndConvertPdfToJpg(String age, String dateFrom, String time, String phone, String childName, Integer idLocation) throws IOException;

    String locationName(Integer idLocation);
}
