package com.maticz.BirthdaysDWH.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "AC_Fact_Summer_Camp")
@Data
public class SummerCamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "campDate")
    private String campDate;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "childName")
    private String childName;

    @Column(name = "cancel")
    private Integer cancel;

    @Column(name = "dailyCare")
    private Integer dailyCare;

    @Column(name = "days")
    private String days;

    @Column(name = "mon")
    private Integer mon;

    @Column(name = "tue")
    private Integer tue;

    @Column(name = "wed")
    private Integer wed;

    @Column(name = "thu")
    private Integer thu;

    @Column(name = "fri")
    private Integer fri;

    @Column(name = "datefrom")
    private LocalDate dateFrom;

    @Column(name = "dateTo")
    private LocalDate dateTo;

    @Column(name = "daysCount")
    private Integer daysCount;

    @Column(name = "idLocation")
    private Integer idLocation;

    @Column(name ="submitDate")
    private LocalDateTime submitDate;

}
