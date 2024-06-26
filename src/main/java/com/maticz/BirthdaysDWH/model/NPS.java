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
@Table(name= "DWH_Fact_NPS")
public class NPS {

    @Id
    @Column(name="NPSId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long NPSId;

    @Column(name="dateNPS")
    private LocalDate dateNPS;

    @Column(name="location")
    private String location;

    @Column(name="NPS")
    @JsonSetter("NPS")
    private Integer NPS;

    @Column(name="email")
    private String email;
}
