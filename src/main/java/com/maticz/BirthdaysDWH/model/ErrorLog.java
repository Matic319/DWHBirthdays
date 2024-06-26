package com.maticz.BirthdaysDWH.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name= "error_log_birthdays")
@Data
public class ErrorLog {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "error_field")
    private String errorField;

    @Column(name = "read_data")
    private String readData;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "sheet")
    private String sheet;

    @Column(name ="dateFrom")
    private LocalDateTime dateFrom;

    @Column(name = "dateTo")
    private LocalDateTime dateTo;

    @Column(name = "parentEmail")
    private String parentEmail;

}
