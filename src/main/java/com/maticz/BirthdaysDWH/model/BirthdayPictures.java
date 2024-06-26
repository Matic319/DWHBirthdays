package com.maticz.BirthdaysDWH.model;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "birthdayPictures")
@AllArgsConstructor
@NoArgsConstructor
public class BirthdayPictures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "parentEmail")
    private String email;

    @Column(name = "dateFrom")
    private LocalDateTime dateFrom;

    @Column(name = "dateTo")
    private LocalDateTime dateTo;

    @Column(name = "idLocation")
    private Integer idLocation;

    @Column(name = "pictureLink")
    private String pictureLink;

    @Column(name = "emailSentFromAC")
    private Integer emailSentFromAC;

    @Column(name = "importTimestamp")
    private LocalDateTime importTimestamp;

    @Column(name = "updateLink")
    private Integer updateLink;

    @Column(name = "emailOpened")
    private Integer emailOpened;

    @Column(name = "idCampaign")
    private Integer idCampaign;

}
