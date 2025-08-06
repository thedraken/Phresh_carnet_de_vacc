package com.phresh.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class VaccinationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private VaccinationType vaccinationType;
    @Temporal(TemporalType.DATE)
    @Column
    private LocalDate scheduledDate;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Vaccination vaccination;

    public VaccinationSchedule(User user, VaccinationType vaccinationType, LocalDate scheduledDate) {
        this.user = user;
        this.vaccinationType = vaccinationType;
        this.scheduledDate = scheduledDate;
    }

    public VaccinationSchedule() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VaccinationType getVaccinationType() {
        return vaccinationType;
    }

    public void setVaccinationType(VaccinationType vaccinationType) {
        this.vaccinationType = vaccinationType;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Vaccination getVaccination() {
        return vaccination;
    }

    public void setVaccination(Vaccination vaccination) {
        this.vaccination = vaccination;
    }

    public boolean isDone() {
        return vaccination != null;
    }
}
