package com.phresh.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class VaccinationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String typeName;
    @Column
    private Integer numberOfDaysBeforeNextVaccination;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Disease> diseasesTreated;
    @Column
    private Integer minAgeForVaccineInDays;
    @Column
    private Double maxAgeForVaccineInYears;
    @Column
    private Integer maxNoOfDoses;

    public VaccinationType() {
    }

    public VaccinationType(String typeName, Integer numberOfDaysBeforeNextVaccination, Set<Disease> diseasesTreated, Integer minAgeForVaccineInDays, Double maxAgeForVaccineInYears, Integer maxNoOfDoses) {
        this.typeName = typeName;
        this.minAgeForVaccineInDays = minAgeForVaccineInDays;
        this.numberOfDaysBeforeNextVaccination = numberOfDaysBeforeNextVaccination;
        this.diseasesTreated = diseasesTreated;
        this.maxAgeForVaccineInYears = maxAgeForVaccineInYears;
        this.maxNoOfDoses = maxNoOfDoses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getNumberOfDaysBeforeNextVaccination() {
        return numberOfDaysBeforeNextVaccination;
    }

    public void setNumberOfDaysBeforeNextVaccination(Integer numberOfDaysBeforeNextVaccination) {
        this.numberOfDaysBeforeNextVaccination = numberOfDaysBeforeNextVaccination;
    }

    public Set<Disease> getDiseasesTreated() {
        return diseasesTreated;
    }

    public void setDiseasesTreated(Set<Disease> diseasesTreated) {
        this.diseasesTreated = diseasesTreated;
    }

    public Integer getMinAgeForVaccineInDays() {
        return minAgeForVaccineInDays;
    }

    public void setMinAgeForVaccineInDays(Integer minAgeForVaccineInDays) {
        this.minAgeForVaccineInDays = minAgeForVaccineInDays;
    }

    public Double getMaxAgeForVaccineInYears() {
        return maxAgeForVaccineInYears;
    }

    public void setMaxAgeForVaccineInYears(Double maxAgeForVaccine) {
        this.maxAgeForVaccineInYears = maxAgeForVaccine;
    }

    public Integer getMaxNoOfDoses() {
        return maxNoOfDoses;
    }

    public void setMaxNoOfDoses(Integer maxNoOfDoses) {
        this.maxNoOfDoses = maxNoOfDoses;
    }

    @Override
    public String toString() {
        return getTypeName();
    }
}
