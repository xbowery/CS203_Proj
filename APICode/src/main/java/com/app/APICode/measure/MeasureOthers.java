package com.app.APICode.measure;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class MeasureOthers {
    @NotNull(message = "Created date should not be null")
    private @Temporal(TemporalType.TIMESTAMP) Date creationDateTime;

    @NotNull(message = "Buisness type should not be null")
    private String businessType;

    @NotNull(message = "Max capacity should not be null")
    private int maxCapacity;

    @NotNull(message = "Vaccination status should not be null")
    private boolean vaccinationStatus;

    // indicates whether mask needs to be worn at the venue
    @NotNull(message = "Mask status should not be null")
    private boolean maskStatus;

    @NotNull(message = "Details of measure should not be null")
    private List<String> details;

    public MeasureOthers(Date creationDateTime, String businessType, int maxCapacity, boolean vaccinationStatus,
    boolean maskStatus, List<String> details) {
        this.creationDateTime = creationDateTime;
        this.businessType = businessType;
        this.maxCapacity = maxCapacity;
        this.vaccinationStatus = vaccinationStatus;
        this.maskStatus = maskStatus;
        this.details = details;
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

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    

}
