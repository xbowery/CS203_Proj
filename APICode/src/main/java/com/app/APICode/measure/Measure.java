package com.app.APICode.measure;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class Measure {
    public Measure() {}
    @Schema(description = "Unique identifier of the Restaurant.", example = "1", required = true)
    private @Id @GeneratedValue Long id;

    @NotNull(message = "Created date should not be null")
    @Schema(description = "Date and Time Measure was created.", example = "Wed Oct 27 12:26:56 SGT 2021", required = true)
    private Date creationDateTime;

    @NotNull(message = "Buisness type should not be null")
    @Schema(description = "Type of Business the measure is targeted for", example = "Gym", required = true)
    private String businessType;

    @NotNull(message = "Max capacity should not be null")
    @Schema(description = "Maximum number of pax permitted for the measure", example = "100", required = true)
    private int maxCapacity;

    @NotNull(message = "Vaccination status should not be null")
    @Schema(description = "If the specified measure requires vaccination", example = "true", required = true)
    private boolean vaccinationStatus;

    // indicates whether mask needs to be worn at the venue
    @Schema(description = "If the specified measure requires mask", example = "false")
    private boolean maskStatus;

    @Schema(description = "Details of the measure", example = "Diners dining need to be 1m apart")
    private String details;

    public Measure(Date creationDateTime, String businessType, int maxCapacity, boolean vaccinationStatus,
    boolean maskStatus, String details) {
        this.creationDateTime = creationDateTime;
        this.businessType = businessType;
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

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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
