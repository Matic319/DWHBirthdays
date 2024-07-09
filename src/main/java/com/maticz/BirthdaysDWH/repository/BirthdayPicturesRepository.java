package com.maticz.BirthdaysDWH.repository;

import com.maticz.BirthdaysDWH.model.BirthdayPictures;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BirthdayPicturesRepository extends JpaRepository<BirthdayPictures, Long> {

    Optional<BirthdayPictures> findByEmailAndDateFromAndIdLocation(String email, LocalDateTime dateFrom, Integer idLocation);

    @Query
            (value = " select parentEmail , picturelink , dateFrom from birthdayPictures \n" +
                    "where updateLink = 1 and idLocation = :idLocation \n ", nativeQuery = true)
    List<Object[]> getEmailAndLink(@Param("idLocation") Integer idLocation);


@Modifying
@Transactional
@Query(value = "update birthdayPictures " +
               "set updateLink = 0 " +
               "where parentEmail = :parentEmail and dateFrom = convert(datetime,:dateFrom ,121) and idLocation = :idLocation ",
       nativeQuery = true)
void updateLink(@Param("idLocation") Integer idLocation,
                @Param("dateFrom") LocalDateTime dateFrom,
                @Param("parentEmail") String parentEmail);

@Query
        (value = " select a.parentEmail, a.dateFrom,a.idCampaign, a.idLocation, b.opened, b.email_sent, a.emailSentFromAC , a.emailOpened from birthdayPictures a " +
                "   left join ac_fact_emails b " +
                "    on a.idCampaign = b.idCampaign and a.parentEmail = b.email and " +
                "  cast(b.email_sent as date) between cast(a.dateFrom as date) and cast(DATEADD(day,14,a.dateFrom) as date) " +
                " where a.emailSentFromAC = 0 or a.emailOpened = 0 " , nativeQuery = true)
    List<Object[]> getContactEmailAndDateFromAndIdLocationAndEmailOpenedAndSentWhereSentAndOpenedZero();


    @Query
            (value = " select * from (\n" +
                    "select a. parentEmail, a. dateFrom,a. idCampaign, a. idLocation, b. opened, b. email_sent, a. emailSentFromAC , a. emailOpened\n" +
                    " from birthdayPictures a \n" +
                    "left join ac_fact_emails b\n" +
                    "on a. idCampaign = b. idCampaign and a. parentEmail = b. email and     \n" +
                    "cast(b. email_sent as date) between cast(a. dateFrom as date) and cast(DATEADD(day,14,a. dateFrom) as date)                  \n" +
                    "     )a where cast(a.datefrom as date) > cast(dateadd(day, -14, GETDATE()) as date) " +
                    " and a.idLocation = :idLocation " +
                    "     order by datefrom asc  " , nativeQuery = true)
    List<Object[]> getContactEmailAndDateFromAndIdLocationAndEmailOpenedAndSent(@Param("idLocation") Integer idLocation);

    @Modifying
    @Transactional
    @Query
            (value = " update birthdayPictures " +
                    " set emailSentFromAC = 1" +
                    " where parentEmail = :parentEmail and dateFrom = convert(datetime,:dateFrom ,121) and idCampaign = :idCampaign  ", nativeQuery = true)
    void updateEmailSentFromAC(@Param("parentEmail") String parentEmail,
                              @Param("dateFrom") LocalDateTime dateFrom,
                              @Param("idCampaign") Integer idCampaign);

    @Modifying
    @Transactional
    @Query
            (value = " update birthdayPictures " +
                    "  set emailOpened = 1" +
                    " where parentEmail = :parentEmail and dateFrom = convert(datetime,:dateFrom, 121) and idCampaign = :idCampaign ", nativeQuery = true  )
    void updateEmailOpened(@Param("parentEmail") String parentEmail,
                              @Param("dateFrom") LocalDateTime dateFrom,
                              @Param("idCampaign") Integer idCampaign);
}
