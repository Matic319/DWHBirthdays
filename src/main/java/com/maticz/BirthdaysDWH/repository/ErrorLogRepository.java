package com.maticz.BirthdaysDWH.repository;

import com.maticz.BirthdaysDWH.model.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog,Integer> {

}
