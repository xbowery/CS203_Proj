package com.app.APICode.measure;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Measure {
    private @Id @GeneratedValue Long id;

    @NotNull(message = "Created date should not be null")
    private LocalDate dateUpdated;

    @NotNull(message = "Buisness type should not be null")
    private String measureType;

    @NotNull(message = "Max capacity should not be null")
    private int maxCapacity;

    @NotNull(message = "Vaccination status should not be null")
    private boolean vaccinationStatus;

    // indicates whether mask needs to be worn at the venue
    @NotNull(message = "Mask status should not be null")
    private boolean maskStatus;

    private String details;

    public Measure() {
    }

    public Measure(String measureType, int maxCapacity, boolean vaccinationStatus, boolean maskStatus, String details) {
        this.dateUpdated = LocalDate.now();
        this.measureType = measureType;
        this.maxCapacity = maxCapacity;
        this.vaccinationStatus = vaccinationStatus;
        this.maskStatus = maskStatus;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDate dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;

    }

    public boolean isVaccinationStatus() {
        return vaccinationStatus;
    }

    public void setVaccinationStatus(boolean vaccinationStatus) {
        this.vaccinationStatus = vaccinationStatus;
    }

    public boolean isMaskStatus() {
        return maskStatus;
    }

    public void setMaskStatus(boolean maskStatus) {
        this.maskStatus = maskStatus;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
