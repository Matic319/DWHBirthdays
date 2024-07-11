package com.maticz.BirthdaysDWH.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}