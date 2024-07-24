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

    @Column(name = "animatorEmail")
    private String animatorEmail;

    @Column(name = "animatorInviteSent")
    private Integer animatorInviteSent;

    @Column(name = "animatorInviteResponse")
    private String animatorInviteResponse;

    @Column(name = "eventId")
    private String eventId;

    @Column(name = "extraAnimatorEmail")
    private String extraAnimatorEmail;

    @Column(name = "extraAnimatorResponse")
    private Integer extraAnimatorResponse;

    @Column(name = "extraAnimatorInviteSent")
    private Integer extraAnimatorInviteSent;

    @Column(name = "eventIdExtraAnimator")
    private String eventIdExtraAnimator;

    @Column(name = "partyPlaceName")
    private String partyPlaceName;

    @Column(name = "food")
    private String food;

    @Column(name = "desserts")
    private String desserts;

    @Column(name = "minAge")
    private Integer minAge;

    @Column(name = "maxAge")
    private Integer maxAge;

    @Column(name = "participantCount")
    private Integer participantCount;

    @Column(name = "comments")
    private String comments;

    @Column(name = "attractionComments")
    private String attractionComments;

    @Column(name = "duration")
    private String duration;

    @Column(name = "partyType")
    private String partyType;

    @Column(name = "parentFirstName")
    private String parentFirstName;

    @Column(name = "parentLastName")
    private String parentLastName;

    @Column(name = "dateTo")
    private LocalDateTime dateTo;

}