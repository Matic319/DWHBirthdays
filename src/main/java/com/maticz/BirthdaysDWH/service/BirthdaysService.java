package com.maticz.BirthdaysDWH.service;

import com.maticz.BirthdaysDWH.model.Birthdays;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public interface BirthdaysService {



    void mapSheetCopyArenaAndSave(String sheetID, String sheetName) throws IOException;

    void mapSheetCopyMS(String sheetId, String sheetName) throws IOException;

    void mapSheetCopyTP(String sheetId,String sheetName) throws IOException;

    void mapSheetTPSept(String sheetId, String sheetName) throws IOException;

    void mapSheetCopyKarting(String sheetId,String sheetName) throws IOException;

    void mapSheetCopyMB(String sheetId,String sheetName) throws IOException;

    void mapSheetCopyRU(String sheetId,String sheetName) throws IOException;

    void mapSheetImportSaveToDB(String sheetId, String sheetName, Integer idLocation) throws IOException;

    List<Birthdays> mapSheetImportSaveToDBFromDate(String sheetId, String sheetName, Integer idLocation, String fromDate) throws IOException;

    void mapAndSaveUpcomingBirthdays(String sheetId,String sheetName, Integer idLocation) throws IOException;

    void getDatesAndTimeForBirthdays(String sheetId, String sheetName) throws IOException;

}
