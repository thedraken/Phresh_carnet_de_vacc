package com.phresh.repository;

import com.google.inject.Inject;
import com.phresh.SQLLiteConfigure;
import com.phresh.SessionHolder;
import com.phresh.domain.VaccinationType;

public class VaccinationTypeRepository extends SessionHolder<VaccinationType> {

    @Inject
    public VaccinationTypeRepository(SQLLiteConfigure sqlLiteConfigure) {
        super(sqlLiteConfigure, VaccinationType.class);
    }
}
