package com.phresh.domain;

import jakarta.persistence.*;

@Entity
public class RoleAccess {

    public final static String ROLE_ADMIN = "ROLE_ADMIN";
    public final static String ROLE_USER = "ROLE_USER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String accessName;

    public RoleAccess() {
    }

    public RoleAccess(String accessName) {
        this.accessName = accessName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }
}
