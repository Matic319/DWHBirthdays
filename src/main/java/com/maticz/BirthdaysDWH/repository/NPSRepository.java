package com.maticz.BirthdaysDWH.repository;

import com.maticz.BirthdaysDWH.model.NPS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface NPSRepository extends JpaRepository<NPS,Long> {

    @Query
            (value = "select count(*) from dwh_fact_nps where dateNPS = :date and email like :email " , nativeQuery = true)
           Integer getCountByDateAndEmail(@Param("date") LocalDate date, @Param("email") String email);

}

