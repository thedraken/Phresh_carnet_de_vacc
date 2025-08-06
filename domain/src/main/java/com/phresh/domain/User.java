package com.phresh.domain;

import jakarta.persistence.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Set;

@Entity
public class User implements UserDetails, Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @Column(nullable = false)
    private boolean enabled = true;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Vaccination> vaccinations;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<VaccinationSchedule> vaccinationSchedules;
    @Transient
    private boolean seenFirstNotification = false;

    public User() {
    }

    public User(String firstName, String surname, String email, String password, Set<Role> roles, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFullName() {
        return getFirstName() + " " + getSurname();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Vaccination> getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(Set<Vaccination> vaccinations) {
        this.vaccinations = vaccinations;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public Integer getAgeInDays() {
        return Period.between(dateOfBirth, LocalDate.now()).getDays();
    }

    public Set<VaccinationSchedule> getVaccinationSchedules() {
        return vaccinationSchedules;
    }

    public void setVaccinationSchedules(Set<VaccinationSchedule> vaccinationSchedules) {
        this.vaccinationSchedules = vaccinationSchedules;
    }

    public boolean isSeenFirstNotification() {
        return seenFirstNotification;
    }

    public void setSeenFirstNotification(boolean seenFirstNotification) {
        this.seenFirstNotification = seenFirstNotification;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return getPassword();
    }

    @Override
    public Object getDetails() {
        return getFullName();
    }

    @Override
    public Object getPrincipal() {
        return getEmail();
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return getFullName();
    }
}
