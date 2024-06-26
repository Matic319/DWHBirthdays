package com.maticz.BirthdaysDWH.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="DWH_Fact_NPS_Birthdays")
public class NPSBirthdays {

    @Column(name="dateNPSBDay")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNPSBDay;

    @Column(name = "location")
    private String location;

    @Column(name = "bDayNPS")
    @JsonSetter("bDayNPS")
    private Integer bDayNPS;

    @Column(name = "birthdayPartyType")
    private String birthDayPartyType;

    @Column(name = "animator")
    private String animator;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="NPSBirthdayId")
    private Long NPSBirthdayId;


    @Column(name = "email")
    private String email;
}
