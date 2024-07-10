package com.maticz.BirthdaysDWH.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PDFTextInsertionService {

     void insertTextIntoPdf(String inputFilePath, String outputFilePath, String age,
                                  String dateFrom, String time,
                                  String phone, String childName, Integer idLocation) throws IOException;

     void convertPdfToJpg(String inputFilePath, String outputFilePrefix, int dpi) throws IOException;
}
