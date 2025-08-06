package com.phresh.repository;

import com.phresh.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {


    Role findRoleByName(String name);

    List<Role> findAll();
}
