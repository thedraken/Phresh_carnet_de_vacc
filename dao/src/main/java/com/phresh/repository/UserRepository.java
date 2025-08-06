package com.phresh.repository;

import com.google.inject.Inject;
import com.phresh.SQLLiteConfigure;
import com.phresh.SessionHolder;
import com.phresh.domain.User;


public class UserRepository extends SessionHolder<User> {

    @Inject
    public UserRepository(SQLLiteConfigure sqlLiteConfigure) {
        super(sqlLiteConfigure, User.class);
    }

    public User findUserByEmailAndEnabledTrue(String email) {
        return null;
    }

    public User findUserByEmail(String email) {
        return null;
    }

}
