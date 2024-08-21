package com.maticz.BirthdaysDWH.service.invites;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.IOException;
@Service
public interface InviteTextInsertionService {

    byte[] createPdfInMemoryInvite(String age, String date, String startTime, Integer idLocation, String endTime,
                                   String phone, String childName, Integer idProgType) throws IOException;

    void insertTextIntoInviteTemplate(String age, String date, String startTime, Integer idLocation, String endTime, String phone,
                                      String childName, Integer idProgType, PDPageContentStream contentStream, float pageHeightInPoints, PDDocument document) throws IOException;


    Context setTextForEmail(String dateFrom, String startTime, String progType,
                            String participantCount, String parentName, String phone,
                             String endTime, String parentComments,
                            String minAge, String maxAge, Integer idProgType, Integer idPartyType, Boolean animatorRequired, String partyType);
}
