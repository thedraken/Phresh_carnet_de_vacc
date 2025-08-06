package com.phresh.repository;

import com.google.inject.Inject;
import com.phresh.SQLLiteConfigure;
import com.phresh.SessionHolder;
import com.phresh.domain.User;
import com.phresh.domain.VaccinationSchedule;
import com.phresh.domain.VaccinationType;

import java.util.ArrayList;
import java.util.List;

public class VaccinationScheduleRepository extends SessionHolder<VaccinationSchedule> {

    @Inject
    public VaccinationScheduleRepository(SQLLiteConfigure sqlLiteConfigure) {
        super(sqlLiteConfigure, VaccinationSchedule.class);
    }

    public List<VaccinationSchedule> findAllByVaccinationTypeAndUserAndVaccinationIsNull(VaccinationType vaccinationType, User user) {
        return new ArrayList<>();
    }

    public List<VaccinationSchedule> findAllByUserAndVaccinationIsNull(User user) {
        return new ArrayList<>();
    }
}
