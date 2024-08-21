package com.maticz.BirthdaysDWH.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DWH_Fact_Birthday_Prices")
@Data
public class BirthdayPrices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "idBirthdayProgType")
    private Integer idBirthdayProgType;

    @Column(name = "idPartyType")
    private Integer idPartyType;

    @Column(name = "pricePerPerson")
    private Float pricePerPerson;

}
