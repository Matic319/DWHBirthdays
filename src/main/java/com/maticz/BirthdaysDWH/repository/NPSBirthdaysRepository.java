package com.maticz.BirthdaysDWH.repository;

import com.maticz.BirthdaysDWH.model.NPSBirthdays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface NPSBirthdaysRepository extends JpaRepository<NPSBirthdays, Long> {


    Optional<NPSBirthdays> findByEmailAndDateNPSBDay(String email , LocalDate npsDate);
}
