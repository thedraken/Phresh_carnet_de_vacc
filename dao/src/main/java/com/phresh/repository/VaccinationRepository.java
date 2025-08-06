package com.phresh.repository;

import com.google.inject.Inject;
import com.phresh.SQLLiteConfigure;
import com.phresh.SessionHolder;
import com.phresh.domain.Disease;
import com.phresh.domain.User;
import com.phresh.domain.Vaccination;
import com.phresh.domain.VaccinationType;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;


public class VaccinationRepository extends SessionHolder<Vaccination> {

    @Inject
    public VaccinationRepository(SQLLiteConfigure sqlLiteConfigure) {
        super(sqlLiteConfigure, Vaccination.class);
    }

    public List<Vaccination> findVaccinationsByFilter(String freeText, LocalDate fromDate, LocalDate toDate, Disease diseaseTreated, VaccinationType vaccinationType, User user) {

        String hqlQuery = "SELECT v FROM Vaccination v JOIN v.vaccinationType vt JOIN vt.diseasesTreated d " +
                "WHERE :user = v.user AND " +
                "(:fromDate IS NULL OR v.dateOfVaccination >= :fromDate) AND " +
                "(:toDate IS NULL OR v.dateOfVaccination <= :toDate) AND " +
                "(:vaccinationType IS NULL OR vt = :vaccinationType) AND " +
                "(:diseaseTreated IS NULL OR d = :diseaseTreated) AND " +
                "(:freeText IS NULL OR v.comments like :freeText OR vt.typeName like :freeText OR d.name like :freeText) )";

        Query<Vaccination> vaccinationQuery = createQuery(hqlQuery);
        vaccinationQuery.setParameter("user", user);
        vaccinationQuery.setParameter("fromDate", fromDate);
        vaccinationQuery.setParameter("toDate", toDate);
        vaccinationQuery.setParameter("vaccinationType", vaccinationType);
        vaccinationQuery.setParameter("diseaseTreated", diseaseTreated);
        vaccinationQuery.setParameter("freeText", "%" + freeText + "%");

        return vaccinationQuery.list();
    }

    public Integer countVaccinationByVaccinationTypeAndUser(VaccinationType vaccinationType, User user) {
        return 0;
    }

}
