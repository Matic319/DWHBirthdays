package com.maticz.BirthdaysDWH.repository;

import com.maticz.BirthdaysDWH.model.BirthdayInvitations;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BirthdayInvitationsRepository extends JpaRepository<BirthdayInvitations,Integer> {


    Optional<BirthdayInvitations> findByEmailAndDateFromAndIdLocationAndChildName(String email, LocalDateTime dateFrom, Integer idLocation , String childName);
    Optional<BirthdayInvitations> findByEmailAndDateFromAndChildNameAndIdLocationAndAnimatorEmail(
            String email, LocalDateTime dateFrom, String childName, Integer idLocation, String animatorEmail );

    Optional<BirthdayInvitations> findByEmailAndDateFromAndChildNameAndIdLocationAndExtraAnimatorEmail(
            String email, LocalDateTime dateFrom, String childName, Integer idLocation, String extraAnimatorEmail );


    @Query
            (value ="select * from birthdayInvitations \n" +
                    "where emailSent = 0 and idLocation = :idLocation " , nativeQuery = true)
    List<Object[]> getAllClientsThatHaveNotReceivedTheInvitation(@Param("idLocation") Integer idLocation);

    @Transactional
    @Modifying
    @Query
            (value ="update birthdayInvitations " +
                    "set emailSent = 1" +
                    " where email = :email and dateFrom = convert(datetime,:dateFrom,121) and idLocation = :idLocation and childName = :childName" , nativeQuery = true)
    void updateEmailSent(@Param("idLocation") Integer idLocation,
                         @Param("dateFrom") LocalDateTime dateFrom,
                         @Param("email") String email,
                         @Param("childName") String childName);


    @Transactional
    @Modifying
    @Query
            (value ="update birthdayInvitations " +
                    " set animatorInviteSent = 1 , eventId = :eventId " +
                    " where email = :parentEmail and dateFrom = convert(datetime, :dateFrom ,121) and idLocation = :idLocation and childName = :childName " , nativeQuery = true)
    void updateAnimatorInviteSentToTrueAndAddEventId(@Param("parentEmail") String parentEmail ,@Param("dateFrom") LocalDateTime dateFrom,
                                                     @Param("idLocation") Integer idLocation,
                                                     @Param("childName") String childName,
                                                     @Param("eventId") String eventId);




        @Transactional
        @Modifying
        @Query
                (value = " update birthdayInvitations " +
                        " set animatorEmail = :animatorEmail , animatorInviteSent = 0 " +
                        " where email = :parentEmail and dateFrom = convert(datetime, :dateFrom, 121) and idLocation = :idLocation " +
                        " and childName = :childName " , nativeQuery = true)
    void updateValueForEmailAnimatorAndSetInviteSentToZero(@Param("parentEmail") String parentEmail,
                                                           @Param("dateFrom") LocalDateTime dateFrom,
                                                           @Param("idLocation") Integer idLocation,
                                                           @Param("childName") String childName,
                                                           @Param("animatorEmail") String animatorEmail);

    @Transactional
    @Modifying
    @Query
            (value = " update birthdayInvitations " +
                    " set extraAnimatorEmail = :extraAnimatorEmail , extraAnimatorInviteSent = 0 " +
                    " where email = :parentEmail and dateFrom = convert(datetime,:dateFrom,121) and idLocation = :idLocation " +
                    " and childName = :childName " , nativeQuery = true)
    void updateValueForExtraAnimatorEmailAndSetInviteSentToZero(@Param("parentEmail") String parentEmail,
                                                                @Param("dateFrom") LocalDateTime dateFrom,
                                                                @Param("idLocation") Integer idLocation,
                                                                @Param("childName") String childName,
                                                                @Param("extraAnimatorEmail") String extraAnimatorEmail);



    @Transactional
    @Modifying
    @Query
            (value ="update birthdayInvitations " +
                    "set extraAnimatorInviteSent = 1 , eventIdExtraAnimator = :eventIdExtraAnimator" +
                    " where email = :email and dateFrom = convert(datetime,:dateFrom,121) and idLocation = :idLocation and childName = :childName" , nativeQuery = true)
    void updateExtraAnimatorInviteSentToTrueAndAddEventId(@Param("email") String email,
                                                     @Param("dateFrom") LocalDateTime dateFrom,
                                                     @Param("idLocation") Integer idLocation,
                                                     @Param("childName") String childName,
                                                     @Param("eventIdExtraAnimator") String eventIdExtraAnimator);

    @Query
            (value = " select * from birthdayInvitations " +
                    " where email = :parentEmail and dateFrom = convert(datetime,:dateFrom,121) and idLocation = :idLocation " +
                    " and childName = :childName and animatorInviteSent = 1 " , nativeQuery = true )
    Optional<BirthdayInvitations> hasAnimatorInviteBeenSent(@Param("parentEmail") String parentEmail,
                                                           @Param("dateFrom") LocalDateTime dateFrom,
                                                           @Param("idLocation") Integer idLocation,
                                                           @Param("childName") String childName);

    @Query
            (value = " select * from birthdayInvitations " +
                    " where email = :parentEmail and dateFrom = convert(datetime,:dateFrom,121) and idLocation = :idLocation " +
                    " and childName = :childName and extraAnimatorInviteSent = 1 " , nativeQuery = true )
    Optional<BirthdayInvitations> hasExtraAnimatorInviteBeenSent(@Param("parentEmail") String parentEmail,
                                                            @Param("dateFrom") LocalDateTime dateFrom,
                                                            @Param("idLocation") Integer idLocation,
                                                            @Param("childName") String childName);

    @Transactional
    @Modifying
    @Query
            (value = " update birthdayInvitations " +
                    " set animatorInviteResponse = :responseInt " +
                    " where eventId = :eventId" , nativeQuery = true )
    void updateAnimatorInviteResponse(@Param("responseInt") Integer responseInt ,
                                      @Param("eventId") String eventId);

    @Transactional
    @Modifying
    @Query
            (value = " update birthdayInvitations " +
                    " set extraAnimatorResponse = :responseInt " +
                    " where eventId = :eventId" , nativeQuery = true )
    void updateExtraAnimatorInviteResponse(@Param("responseInt") Integer responseInt ,
                                      @Param("eventId") String eventId);

@Query
        ( value = "select eventId from birthdayInvitations\n" +
                "where animatorEmail = :animatorEmail\n" +
                "and idLocation = :idLocation and  isnull(animatorInviteResponse,-1) = -1" +
                " and dateFrom = convert(datetime, :dateFrom , 121) ", nativeQuery = true)
    String getInviteIdWhereThereIsNoResponse(@Param("animatorEmail") String animatorEmail,
                                                          @Param("idLocation") Integer idLocation,
                                                          @Param("dateFrom") LocalDateTime dateFrom);


}
