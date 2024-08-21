package com.maticz.BirthdaysDWH.service.invites;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface InviteService {

    void setDataForInvite(Integer idLocation) throws IOException;
}
