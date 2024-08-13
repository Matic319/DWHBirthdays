package com.maticz.BirthdaysDWH.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "DWH_Fact_Birthdays")
public class Birthdays {

    @Id
    @Column(name = "idBirthday")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBirthday;


    @Column(name = "dateFrom")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateFrom;

    @Column(name = "dateTo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTo;

    @Column(name = "birthdayProgType")
    private String birthdayProgType;

    @Column(name = "idBDayProgType")
    private Integer idBDayProgType;

    @Column(name = "birthdayPartyType")
    private String birthdayPartyType;

    @Column(name = "idBDayPartyType")
    private Integer idBDayPartyType;

    @Column(name = "duration")
    private String duration;

    @Column(name = "idLocation")
    private Integer idLocation;

    @Column(name = "active")
    private Integer active;

    @Column(name = "parentEmail")
    private String parentEmail;

    @Column(name = "parentFirstName")
    private String parentFirstName;

    @Column(name = "parentLastName")
    private String parentLastName;

    @Column(name = "childFirstName")
    private String childFirstName;

    @Column(name = "childLastName")
    private String childLastName;

    @Column(name = "childBDayAge")
    private Integer childBDayAge;

    @Column(name = "participantCount")
    private Integer participantCount;

    @Column(name = "animator")
    private String animator;

    @Column(name = "idPlace")
    private Integer idPlace;

    @Column(name = "idPartyPlaceName")
    private String idPartyPlaceName;

    @Column(name = "idContactResType")
    private Integer idContactResType;

    @Column(name = "idCancelationType")
    private Integer idCancelationType;

    @Column(name = "cancelationReasonName")
    private String cancelationReasonName;

    @Column(name = "dateCanceled")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate dateCanceled;

    @Column(name="DodatniAnimator")
    private String dodatniAnimator;

    @Column(name = "upcoming")
    private Integer upcoming;

    @Column(name = "durationTime")
    private Integer durationTime;

    @Column(name = "idExtraProgram")
    private Integer idExtraProgram;

    @Column(name ="idExtraProgramSubType")
    private Integer idExtraProgramSubType;

    @Column(name = "phone")
    private String phone;
}