package com.maticz.BirthdaysDWH.service;

import java.io.IOException;

public interface SummerCampService {

    void mapSheetHolidaysAndSaveToDBAndAC(String idSheet, String sheetName, String range, Integer idLocation) throws IOException;

    void copyValuesToSheet(String idSheet, String sheetName, String range) throws IOException;

    void copy23data(String idSheet, String sheetName, String range, Integer idLocation) throws IOException;

}
