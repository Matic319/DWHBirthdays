package com.maticz.BirthdaysDWH.repository;

import com.maticz.BirthdaysDWH.model.Birthdays;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BirthdaysRepository extends JpaRepository<Birthdays,Long> {

    Optional<Birthdays> findByIdBirthday(Long idBirthday);

    Optional<Birthdays> findByParentEmailAndDateFrom(String parentEmail, LocalDateTime dateFrom);

    Optional<Birthdays> findByParentEmailAndDateFromAndChildFirstNameAndIdLocation(String parentEmail, LocalDateTime dateFrom, String childFirstName, Integer idLocation);



    Optional<Birthdays> findByParentEmailAndDateFromAndIdLocationAndActiveAndChildFirstName(String parentEmail, LocalDateTime datefrom , Integer idLocation, Integer active , String childFirstName);



    @Modifying
    @Transactional
    @Query
            (value = "delete from DWH_Fact_Birthdays\n" +
                    "where  upcoming = 1\n" +
                    "and idLocation = :idLocation " , nativeQuery = true)
    void deleteUpcomingBdays(@RequestParam("idLocation") Integer idLocation);

    @Query
            (value = " select dateFrom, \n" +
                    " CONCAT( \n" +
                    "  BirthdayProgType, ' ',  \n" +
                    "  birthdayPartyType, ' ') program ,  \n" +
                    " concat(  case  \n" +
                    "    when idExtraProgram = 2 then 'Cosmic' \n" +
                    "    when idExtraProgram = 1 then ( \n" +
                    "                                    select extraProgramName  \n" +
                    "                                    from DWH_Ref_extraPartyProgram b  \n" +
                    "                                    where a.idExtraProgram = b.idExtraProgram  \n" +
                    "                                      and a.idExtraProgramSubType = b.idExtraProgramSubType \n" +
                    ") \n" +
                    "    else ' ' END, ' ',\n" +
                    "     case  \n" +
                    "      when idExtraProgramSubType is not null then ( \n" +
                    "         select extraProgramSubTypeName  \n" +
                    "            from DWH_Ref_extraPartyProgram c  \n" +
                    "               where a.idExtraProgram = c.idExtraProgram  \n" +
                    "                 and a.idExtraProgramSubType = c.idExtraProgramSubType \n" +
                    "               ) \n" +
                    "    else ' ' end ) as progSubName, \n" +
                    "idPartyPlaceName,  \n" +
                    "childFirstName,  \n" +
                    "childLastName,  \n" +
                    "childBDayAge,  \n" +
                    "phone,  \n" +
                    "participantCount,\n" +
                    "parentFirstName,\n" +
                    "minAge,\n" +
                    "maxAge,\n" +
                    "inviteComments,\n" +
                    "dateTo, animator\n" +
                    "\n" +
                    "  from DWH_Fact_Birthdays a \n" +
                    " where upcoming = 1  \n" +
                    "  and idLocation = :idLocation  \n" +
                    "  and cast(dateFrom as date) = cast(dateadd(day, 1, getdate()) as date) ", nativeQuery = true)
    List<Object[]> getBdayFormData(@Param("idLocation") Integer idLocation);

}
