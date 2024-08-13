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
            (value = " select dateFrom \n" +
                    ",birthdayProgType + ' ' +birthdayPartyType +' ' +\n" +
                    "case when idExtraProgram = 2 then 'Cosmic'\n" +
                    "\twhen idextraProgram = 1 then  \n" +
                    "\t\t(select extraProgramName from DWH_Ref_extraPartyProgram b where a.idExtraProgram = b.idExtraProgram and a.idExtraProgramSubType = b.idExtraProgramSubType)\n" +
                    "\t\telse '' end\n" +
                    "+ ' ' + case when idExtraProgramSubType is not null then\n" +
                    "\t\t(select extraProgramSubTypeName from DWH_Ref_extraPartyProgram c where a.idExtraProgram = c.idExtraProgram and a.idExtraProgramSubType = c.idExtraProgramSubType) else '' end progName\n" +
                    ",idPartyPlaceName, childFirstName, childLastName, childBDayAge, phone, \n" +
                    "   participantCount   \n" +
                    "    from DWH_Fact_Birthdays a\n" +
                    " where upcoming = 1 and idLocation = :idLocation \n" +
                    "                    and cast(dateFrom as date) = cast(GETDATE() as date) ", nativeQuery = true)
    List<Object[]> getBdayFormData(@Param("idLocation") Integer idLocation);

}
