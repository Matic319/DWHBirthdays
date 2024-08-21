package com.maticz.BirthdaysDWH.service.invites.impl;

import com.google.j2objc.annotations.AutoreleasePool;
import com.maticz.BirthdaysDWH.repository.BirthdayInvitationsRepository;
import com.maticz.BirthdaysDWH.service.invites.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InviteServiceImpl implements InviteService {
    @Autowired
    BirthdayInvitationsRepository birthdayInvitationsRepository;

    @Autowired
            InviteTextInsertionServiceImpl inviteTextInsertionService;

    DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    DateTimeFormatter formatterInviteDate = DateTimeFormatter.ofPattern("d.M.yyyy");
    DateTimeFormatter formatterInviteTime = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void setDataForInvite(Integer idLocation) throws IOException {
        List<Object[]> inviteData = birthdayInvitationsRepository.getInviteData(idLocation);

        for(Object[] row : inviteData) {
            String parentEmail = row[0].toString();
            String childName = row[1].toString();
            String age = row[2].toString();
            String phone = row[3].toString();
            String minAge = row[4].toString();
            String maxAge = row[5].toString();
            String participantCount = row[6].toString();
            String partyType = row[7] != null ? row[7].toString() : null ;
            String programType = row[8] != null ? row[8].toString() : null;
            String parentFirstName = row[9].toString();
            int requiredAnimator = Integer.parseInt(row[10].toString());
            Integer idBirthdayProgType = Integer.parseInt(row[11].toString());
            Integer idPartyType = Integer.parseInt(row[12].toString());
            String parentComments = row[13] != null ? row[13].toString() : null ;
            LocalDateTime dateFrom = LocalDateTime.parse(row[14].toString(), formatterDB);
            LocalDateTime dateTo = LocalDateTime.parse(row[15].toString(), formatterDB);

            String inviteDate = dateFrom.format(formatterInviteDate);
            String startTime = dateFrom.format(formatterInviteTime);
            String endTime = dateTo.format(formatterInviteTime);

            Boolean animatorRequired = false;
            if(requiredAnimator == 1) {
                animatorRequired = true;
            }else {
                animatorRequired = false;
            }

           Context context = inviteTextInsertionService.setTextForEmail(inviteDate,startTime,programType,participantCount,parentFirstName,phone,
                    endTime,parentComments,minAge,maxAge,idBirthdayProgType,idPartyType,animatorRequired
                    ,partyType);
            byte[] inviteAttachment = inviteTextInsertionService.createPdfInMemoryInvite(age,inviteDate,startTime,idLocation,
                    endTime,phone,childName,idBirthdayProgType);



        }
    }
}
