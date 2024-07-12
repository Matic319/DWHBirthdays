package com.maticz.BirthdaysDWH.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maticz.BirthdaysDWH.repository.BirthdayPicturesRepository;
import com.maticz.BirthdaysDWH.service.ACService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ACServiceImpl implements ACService {

    @Value("${ac.api.token}")
    private String acToken;

    Logger logger = LoggerFactory.getLogger(ACServiceImpl.class);

    DateTimeFormatter formatterISOOffset = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Autowired
    BirthdayPicturesRepository birthdayPicturesRepository;


    @Override
    public void sendToAC(String email, String cancellationDate, Integer idLocation, Boolean secondDate) throws JsonProcessingException {


        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://woop.activehosted.com/api/3/contact/sync";

        HttpHeaders headers = new HttpHeaders();
        headers.set("API-Token", acToken);

        List<Map<String, Object>> fieldValues = new ArrayList<>();

        if (idLocation == 1) {
            if (!secondDate) {
                fieldValues.add(createFieldValueMap("253", cancellationDate));
            } else {
                fieldValues.add(createFieldValueMap("254", cancellationDate));
            }
        } else {
            if (!secondDate) {
                fieldValues.add(createFieldValueMap("255", cancellationDate));
            } else {
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


    @Override
    public JsonNode getContactActivitiesAfterDate(String idSubscriber, String date) throws JsonProcessingException {
        final int MAX_RETRIES = 3;
        final long RETRY_DELAY = TimeUnit.SECONDS.toMillis(1);

        int retryCount = 0;
        boolean success = false;

        ResponseEntity<String> response = null;
        ObjectMapper objectMapper = null;
        while (!success && retryCount < MAX_RETRIES) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String baseUrl = "https://woop.api-us1.com/api/3/activities?contact=" + idSubscriber + "&after=" + date + "&orders[tstamp]=asc&api_output=json&limit=1000";
                HttpHeaders headers = new HttpHeaders();
                headers.set("Api-Token", acToken);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class);
                success = true;
                objectMapper = new ObjectMapper();

            } catch (HttpServerErrorException e) {
                System.err.println(" failed: " + e.getMessage());

                retryCount++;

                if (retryCount < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        return objectMapper.readTree(response.getBody());
    }

    @Override
    public void updateEmailSentIfMatchFoundInActivities(String email, LocalDateTime dateFrom, Integer idCampaign) throws JsonProcessingException {

        Integer idSubscriber = birthdayPicturesRepository.getIdSubscriber(email);
        String idSubscriberString;

        if(idSubscriber != null){
             idSubscriberString = String.valueOf(idSubscriber);
             String date = dateFrom.toLocalDate().toString();
            JsonNode node = getContactActivitiesAfterDate(idSubscriberString, date);

            System.out.println(node.toString());

            if (node.has("logs")) {
                JsonNode logs = node.path("logs");
                for (JsonNode log : logs) {
                    Integer logsIdCampaign = log.path("campaignid").asInt();
                    String tstamp = log.path("tstamp").asText();

                    OffsetDateTime originalTimestamp = OffsetDateTime.parse(tstamp, formatterISOOffset);
                    LocalDateTime convertedTimestamp = originalTimestamp.atZoneSameInstant(ZoneId.of("Europe/Ljubljana")).toLocalDateTime();

                    System.out.println("Campaign logs ID: " + logsIdCampaign + ", campaignId:" + idCampaign + ", TimestampConverted: " + convertedTimestamp + "date:" + date);

                    if (logsIdCampaign.equals(idCampaign) && convertedTimestamp.toLocalDate().isAfter(LocalDate.parse(date).minusDays(1))) {
                        birthdayPicturesRepository.updateEmailSentFromAC(email,dateFrom,idCampaign);
                        break;
                    }
                }
            }
        }

    }

}