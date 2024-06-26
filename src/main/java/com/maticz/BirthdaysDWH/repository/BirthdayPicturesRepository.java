package com.maticz.BirthdaysDWH.repository;

import com.maticz.BirthdaysDWH.model.BirthdayPictures;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BirthdayPicturesRepository extends JpaRepository<BirthdayPictures, Long> {

    Optional<BirthdayPictures> findByEmailAndDateFromAndIdLocation(String email, LocalDateTime dateFrom, Integer idLocation);

    @Query
            (value = " select parentEmail , picturelink , dateFrom from birthdayPictures \n" +
                    "where updateLink = 1 \n ", nativeQuery = true)
    List<Object[]> getEmailAndLink();


@Modifying
@Transactional
@Query(value = "update birthdayPictures " +
               "set updateLink = 0 " +
               "where parentEmail = :parentEmail and dateFrom = convert(datetime,:dateFrom ,121) and idLocation = :idLocation ",
       nativeQuery = true)
void updateLink(@Param("idLocation") Integer idLocation,
                @Param("dateFrom") LocalDateTime dateFrom,
                @Param("parentEmail") String parentEmail);

}
