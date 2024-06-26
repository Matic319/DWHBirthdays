package com.maticz.BirthdaysDWH.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maticz.BirthdaysDWH.service.ACService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ACServiceImpl implements ACService {

    @Value("${ac.api.token}")
    private String acToken;

    Logger logger = LoggerFactory.getLogger(ACServiceImpl.class);

    @Override
    public void sendToAC(String email,String cancellationDate, Integer idLocation, Boolean secondDate) throws JsonProcessingException {


            RestTemplate restTemplate = new RestTemplate();
            String baseUrl = "https://woop.activehosted.com/api/3/contact/sync";

            HttpHeaders headers = new HttpHeaders();
            headers.set("API-Token", acToken);

            List<Map<String, Object>> fieldValues = new ArrayList<>();

            if(idLocation == 1) {
                if(!secondDate) {
                    fieldValues.add(createFieldValueMap("253", cancellationDate));
                } else {
                    fieldValues.add(createFieldValueMap("254", cancellationDate));
                }
            } else {
                if(!secondDate){
                    fieldValues.add(createFieldValueMap("255", cancellationDate));
                }else {
                    fieldValues.add(createFieldValueMap("256", cancellationDate));
                }
            }



            Map<String, Object> contact = new HashMap<>();
            contact.put("email", email);
            contact.put("fieldValues", fieldValues);

            Map<String, Object> body = new HashMap<>();
            body.put("contact", contact);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(body);
            logger.info("Final JSON body sent: {}", json);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, String.class);
            logger.info("API Response: {}", responseEntity.getBody());
        }

    @Override
    public void sendToACPictureLink(String email, String pictureLink) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://woop.activehosted.com/api/3/contact/sync";

        HttpHeaders headers = new HttpHeaders();
        headers.set("API-Token", acToken);

        List<Map<String, Object>> fieldValues = new ArrayList<>();


        fieldValues.add(createFieldValueMap("245", pictureLink));

        Map<String, Object> contact = new HashMap<>();
        contact.put("email", email);
        contact.put("fieldValues", fieldValues);

        Map<String, Object> body = new HashMap<>();
        body.put("contact", contact);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(body);
        logger.info("Final JSON body sent: {}", json);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, String.class);
        logger.info("API Response: {}", responseEntity.getBody());

    }

    private Map<String, Object> createFieldValueMap(String fieldId, String value) {
            Map<String, Object> fieldValueMap = new HashMap<>();
            fieldValueMap.put("field", fieldId);
            fieldValueMap.put("value", value != null ? value : "");
            return fieldValueMap;
        }
    }

