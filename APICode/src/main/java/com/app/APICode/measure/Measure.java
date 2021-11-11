package com.app.APICode.measure;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class Measure {
    @Schema(description = "Unique identifier of the Restaurant.", example = "1", required = true)
    private @Id @GeneratedValue Long id;

    @NotNull(message = "Date updated should not be null")
    @Schema(description = "The Date this Measure was updated/created.", example = "2007-12-03", required = true)
    private LocalDate dateUpdated;

    @NotNull(message = "Measure type should not be null")
    @Schema(description = "Who the measure is targeted for", example = "Gym", required = true)
    private String measureType;

    @NotNull(message = "Max capacity should not be null")
    @Schema(description = "Maximum number of pax permitted for the measure.", example = "100", required = true)
    private int maxCapacity;

    @NotNull(message = "Vaccination status should not be null")
    @Schema(description = "If the specified measure requires vaccination.", example = "true", required = true)
    private boolean vaccinationStatus;

    @NotNull(message = "Mask status should not be null")
    @Schema(description = "If the specified measure requires mask", example = "false")
    private boolean maskStatus;

    public Measure() {
    }

    public Measure(String measureType, int maxCapacity, boolean vaccinationStatus, boolean maskStatus) {
        this.dateUpdated = LocalDate.now();
        this.measureType = measureType;
        this.maxCapacity = maxCapacity;
        this.vaccinationStatus = vaccinationStatus;
        this.maskStatus = maskStatus;
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
}
