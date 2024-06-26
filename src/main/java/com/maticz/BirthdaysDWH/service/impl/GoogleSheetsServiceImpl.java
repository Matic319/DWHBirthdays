package com.maticz.BirthdaysDWH.service.impl;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.maticz.BirthdaysDWH.service.GoogleSheetsService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleSheetsServiceImpl implements GoogleSheetsService {
    private final Sheets sheetsService;

    public GoogleSheetsServiceImpl(Sheets sheetsService) {
        this.sheetsService = sheetsService;
    }

    @Override
    public void writeToSheet(String spreadsheetId, String range , String data) throws IOException {
        List<List<Object>> values = Arrays.asList(
                Arrays.asList(data)
        );

        ValueRange body = new ValueRange().setValues(values);

        sheetsService.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    @Override
    public void appendToSheetLastRow(String spreadsheetId, String range , String data) throws IOException {
        List<List<Object>> values = Arrays.asList(
                Arrays.asList(data)
        );

        ValueRange body = new ValueRange().setValues(values);

        sheetsService.spreadsheets().values()
                .append(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }


    @Override
    public void writeToSheet2(String spreadSheetId, String sheetName, String range, List<List<Object>> valuesToInsert) throws IOException {
        List<ValueRange> data = new ArrayList<>();
        data.add(new ValueRange()
                .setRange(sheetName + "!" + range)
                .setValues(valuesToInsert));

        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest()
                .setValueInputOption("USER_ENTERED")
                .setData(data);

        BatchUpdateValuesResponse batchResult = sheetsService.spreadsheets().values()
                .batchUpdate(spreadSheetId, batchBody)
                .execute();
    }

    @Override
    public Sheets getSheetsService() {
        return this.sheetsService;
    }




    @Override
    public List<List<Object>> readSheetRange(String spreadsheetId, String sheetName) throws IOException {
        String range =   sheetName + "!" + "A4:CP6599";
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();

        return values;
    }

    @Override
    public List<List<Object>> readSheetRangeFrom(String spreadsheetId, String sheetName, String sheetRange) throws IOException {
        String range =   sheetName + "!" + sheetRange;
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();

        return values;
    }


}