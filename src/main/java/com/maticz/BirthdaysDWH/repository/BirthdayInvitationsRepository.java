package com.maticz.BirthdaysDWH.repository;

import com.maticz.BirthdaysDWH.model.BirthdayInvitations;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BirthdayInvitationsRepository extends JpaRepository<BirthdayInvitations,Integer> {


    Optional<BirthdayInvitations> findByEmailAndDateFromAndIdLocationAndChildName(String email, LocalDateTime dateFrom, Integer idLocation , String childName);

    @Query
            (value ="select * from birthdayInvitations \n" +
                    "where emailSent = 0 and idLocation = :idLocation " , nativeQuery = true)
    List<Object[]> getAllClientsThatHaveNotReceivedTheInvitation(@Param("idLocation") Integer idLocation);

    @Transactional
    @Modifying
    @Query
            (value ="update birthdayInvitations " +
                    "set emailSent = 1" +
                    " where email = :email and dateFrom = convert(datetime,:dateFrom,121) and idLocation = :idLocation " , nativeQuery = true)
    void updateEmailSent(@Param("idLocation") Integer idLocation,
                         @Param("dateFrom") LocalDateTime dateFrom,
                         @Param("email") String email);
}
