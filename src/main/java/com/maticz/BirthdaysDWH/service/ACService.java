package com.maticz.BirthdaysDWH.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ACService {

    void sendToAC(String email,String cancellationDate, Integer idLocation, Boolean secondDate) throws JsonProcessingException;

    void sendToACPictureLink(String email, String pictureLink) throws JsonProcessingException;
}
