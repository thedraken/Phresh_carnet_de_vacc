package com.phresh.repository;

import com.google.inject.Inject;
import com.phresh.SQLLiteConfigure;
import com.phresh.SessionHolder;
import com.phresh.domain.Disease;


public class DiseaseRepository extends SessionHolder<Disease> {

    @Inject
    public DiseaseRepository(SQLLiteConfigure sqlLiteConfigure) {
        super(sqlLiteConfigure, Disease.class);
    }
}
