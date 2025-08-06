package com.phresh.repository;

import com.phresh.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByEmailAndEnabledTrue(String email);

    User findUserByEmail(String email);
}
