package com.maticz.BirthdaysDWH.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public interface ACService {

    void sendToAC(String email,String cancellationDate, Integer idLocation, Boolean secondDate) throws JsonProcessingException;

    void sendToACPictureLink(String email, String pictureLink, Integer idLocation) throws JsonProcessingException;

    JsonNode getContactActivitiesAfterDate(String idSubscriber, String date) throws JsonProcessingException;


    void updateEmailSentIfMatchFoundInActivities(String email, LocalDateTime dateFrom, Integer idCampaign) throws JsonProcessingException;
}
