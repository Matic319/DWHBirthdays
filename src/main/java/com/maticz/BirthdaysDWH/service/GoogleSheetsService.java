package com.maticz.BirthdaysDWH.service;

import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.util.List;

public interface GoogleSheetsService {
    void writeToSheet(String spreadsheetId, String range, String data) throws IOException;


    void writeToSheet2(String spreadSheetId, String sheetName, String range, List<List<Object>> valuesToInsert) throws IOException;

    Sheets getSheetsService();


    List<List<Object>> readSheetRange(String spreadsheetId, String sheetName) throws IOException;

    List<List<Object>> readSheetRangeFrom(String spreadsheetId, String sheetName, String sheetRange) throws IOException;

    void appendToSheetLastRow(String spreadsheetId, String range , String data) throws IOException;

    void clearAndInsertValues(String spreadsheetId, String sheetName, String range, List<List<Object>> values) throws IOException;
}
