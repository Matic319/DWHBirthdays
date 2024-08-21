package com.maticz.BirthdaysDWH.repository;

import com.maticz.BirthdaysDWH.model.BirthdayPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BirthdayPricesRepository extends JpaRepository<BirthdayPrices,Integer> {

    @Query
            (value = " select pricePerPerson from DWH_Fact_Birthday_Prices " +
                    "where idBirthdayProgType = :idProgType and idPartyType = :idPartyType " , nativeQuery = true)
    Float getPricePerPerson(@Param("idProgType") Integer idProgType , @Param("idPartyType") Integer idPartyType);

}
