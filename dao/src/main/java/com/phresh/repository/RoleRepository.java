package com.phresh.repository;

import com.google.inject.Inject;
import com.phresh.SQLLiteConfigure;
import com.phresh.SessionHolder;
import com.phresh.domain.Role;

public class RoleRepository extends SessionHolder<Role> {

    @Inject
    public RoleRepository(SQLLiteConfigure sqlLiteConfigure) {
        super(sqlLiteConfigure, Role.class);
    }

    public Role findRoleByName(String name) {
        return null;
    }
}
