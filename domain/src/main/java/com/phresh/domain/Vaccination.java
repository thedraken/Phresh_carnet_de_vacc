package com.phresh.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private VaccinationSchedule scheduledVaccination;

    public Vaccination() {
    }

    public Vaccination(VaccinationType vaccinationType, LocalDate dateOfVaccination, String comments, User user, VaccinationSchedule scheduledVaccination) {
        this.vaccinationType = vaccinationType;
        this.dateOfVaccination = dateOfVaccination;
        this.comments = comments;
        this.user = user;
        this.scheduledVaccination = scheduledVaccination;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VaccinationSchedule getScheduledVaccination() {
        return scheduledVaccination;
    }

    public void setScheduledVaccination(VaccinationSchedule scheduledVaccination) {
        this.scheduledVaccination = scheduledVaccination;
    }
}
