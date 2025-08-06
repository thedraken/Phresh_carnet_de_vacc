package com.phresh.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    @OneToMany()
    private Set<RoleAccess> roleAccess;

    public Role() {
    }

    public Role(String name, Set<RoleAccess> roleAccess) {
        this.name = name;
        this.roleAccess = roleAccess;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RoleAccess> getRoleAccess() {
        return roleAccess;
    }

    public void setRoleAccess(Set<RoleAccess> roleAccess) {
        this.roleAccess = roleAccess;
    }
}
