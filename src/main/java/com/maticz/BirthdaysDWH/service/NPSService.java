package com.maticz.BirthdaysDWH.service;

import com.maticz.BirthdaysDWH.model.NPS;
import com.maticz.BirthdaysDWH.model.NPSBirthdays;

import java.io.IOException;
import java.util.List;

public interface NPSService {

    List<NPS> mapAndSaveToDBNPS(String idSheet, String sheetName, String range) throws IOException;

  //  void mapAndSaveToDBNPSBday(String idSheet, String sheetName, String range) throws IOException;

    List<NPSBirthdays> mapAndSaveToDBNPSBday2(String idSheet, String sheetName, String range) throws IOException;

}

