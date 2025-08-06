package com.phresh.repository;

import com.google.inject.Inject;
import com.phresh.SQLLiteConfigure;
import com.phresh.SessionHolder;
import com.phresh.domain.RoleAccess;

public class RoleAccessRepository extends SessionHolder<RoleAccess> {

    @Inject
    public RoleAccessRepository(SQLLiteConfigure sqlLiteConfigure) {
        super(sqlLiteConfigure, RoleAccess.class);
    }


}
