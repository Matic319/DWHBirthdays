package com.maticz.BirthdaysDWH.repository;

import com.maticz.BirthdaysDWH.model.SummerCamp;
import jakarta.transaction.Transactional;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SummerCampRepository extends JpaRepository<SummerCamp, Integer> {


    @Query(
            value = " select  distinct terminOdpoved, emailOdpoved  \n" +
                    "                      from (  \n" +
                    "                           select imeOdpoved, terminOdpoved, emailOdpoved, c.email, c.childName, c.campDate ,case when a.email is null and emailOdpoved is not null then 1 else 0 end cancell   \n" +
                    "                         from (  \n" +
                    "                                select email, campDate, datefrom, childName ,idlocation  \n" +
                    "                                  from   \n" +
                    "                                  AC_Fact_Summer_Camp   \n" +
                    "                                   where cancel = 0 and datefrom >=  cast(dateadd(day,-7,getdate()) as date )and idlocation = :idLocation ) a            \n" +
                    "                    full outer join   \n" +
                    "                       (select email emailOdpoved, campDate terminOdpoved, datefrom dateOdpoved, childName imeOdpoved, idLocation  \n" +
                    "                         from   \n" +
                    "                        AC_Fact_Summer_Camp   \n" +
                    "                         where cancel = 1 and datefrom >= cast(dateadd(day,-7,getdate()) as date ) and idlocation = :idLocation) b   \n" +
                    "                       on a.email = b.emailOdpoved and a.campDate = b.terminOdpoved and a.datefrom = b.dateOdpoved and a.childName = b.imeOdpoved and a.idlocation = b.idlocation  \n" +
                    "                       left join   \n" +
                    "                             (select * from AC_Fact_Summer_Camp where cancel = 0) c on c.email = b.emailOdpoved and c.campDate = b.terminOdpoved and c.idlocation = b.idlocation ) d   \n" +
                    "                        where cancell = 1 and email is null   ", nativeQuery = true
    )
    List<Object[]> getClientsThatCancelled(@Param("idLocation") Integer idLocation);

    @Modifying
    @Transactional
    @Query
            ( value = " delete from AC_Fact_Summer_Camp where idLocation = :idLocation and year(datefrom) = year(getDate())" , nativeQuery = true)
    void deleteAllByIdLocation(@Param("idLocation") Integer idLocation) ;

}
