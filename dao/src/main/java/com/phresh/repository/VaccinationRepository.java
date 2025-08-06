package com.phresh.repository;

import com.phresh.domain.Disease;
import com.phresh.domain.User;
import com.phresh.domain.Vaccination;
import com.phresh.domain.VaccinationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationRepository extends CrudRepository<Vaccination, Long> {

    @Query("SELECT v FROM Vaccination v JOIN v.vaccinationType vt JOIN vt.diseasesTreated d " +
            "WHERE :user = v.user AND " +
            "(:fromDate IS NULL OR v.dateOfVaccination >= :fromDate) AND " +
            "(:toDate IS NULL OR v.dateOfVaccination <= :toDate) AND " +
            "(:vaccinationType IS NULL OR vt = :vaccinationType) AND " +
            "(:diseaseTreated IS NULL OR d = :diseaseTreated) "
    )
    List<Vaccination> findVaccinationsByFilter(String freeText, LocalDate fromDate, LocalDate toDate, Disease diseaseTreated, VaccinationType vaccinationType, User user);

    Integer countVaccinationByVaccinationTypeAndUser(VaccinationType vaccinationType, User user);

}
