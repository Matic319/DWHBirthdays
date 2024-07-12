package com.maticz.BirthdaysDWH.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "birthdayInvitations")
@NoArgsConstructor
@AllArgsConstructor
public class BirthdayInvitations {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "dateFrom")
    private LocalDateTime dateFrom;

    @Column(name = "idLocation")
    private Integer idLocation;

    @Column(name = "childName")
    private String childName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "emailSent")
    private Integer emailSent;

    @Column(name ="age")
    private Integer age;

    @Column(name = "importTimestamp")
    private LocalDateTime importTimestamp;

    @Column(name = "animatorEmail")
    private String animatorEmail;

    @Column(name = "animatorInviteSent")
    private Integer animatorInviteSent;

    @Column(name = "animatorInviteResponse")
    private String animatorInviteResponse;



}