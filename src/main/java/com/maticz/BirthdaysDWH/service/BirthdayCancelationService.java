package com.maticz.BirthdaysDWH.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface BirthdayCancelationService {

    void mapAndCopyCancelattion(String sheetId, String sheetName, Integer idLocation) throws IOException;

    void copyCancellationFromCopySheets(String sheetId, String sheetName, Integer idLocation) throws IOException;

}
