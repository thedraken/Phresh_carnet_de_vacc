package com.phresh.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private VaccinationType vaccinationType;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfVaccination;

    @Column(length = 500)
    private String comments;

    @Temporal(TemporalType.DATE)
    @Column
    private LocalDate dateOfRenewal;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> users;

    public Vaccination() {
    }

    public Vaccination(VaccinationType vaccinationType, LocalDate dateOfVaccination, String comments, LocalDate dateOfRenewal, List<User> users) {
        this.vaccinationType = vaccinationType;
        this.dateOfVaccination = dateOfVaccination;
        this.comments = comments;
        this.dateOfRenewal = dateOfRenewal;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccinationType getVaccinationType() {
        return vaccinationType;
    }

    public void setVaccinationType(VaccinationType vaccinationType) {
        this.vaccinationType = vaccinationType;
    }

    public LocalDate getDateOfVaccination() {
        return dateOfVaccination;
    }

    public void setDateOfVaccination(LocalDate dateOfVaccination) {
        this.dateOfVaccination = dateOfVaccination;
    }

    public LocalDate getDateOfRenewal() {
        return dateOfRenewal;
    }

    public void setDateOfRenewal(LocalDate dateOfRenewal) {
        this.dateOfRenewal = dateOfRenewal;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUser(List<User> users) {
        this.users = users;
    }
}
