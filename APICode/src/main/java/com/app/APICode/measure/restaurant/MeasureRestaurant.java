package com.app.APICode.measure.restaurant;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class MeasureRestaurant {
    private @Id @GeneratedValue Long id;

    @NotNull (message = "Created date should not be null")
    private Date creationDateTime;

    @NotNull(message = "Max capacity should not be null")
    private int maxPerTable;

    @NotNull(message = "Vaccination status should not be null")
    private boolean vaccinationStatus;

    @NotNull(message = "Dining in status should not be null")
    private boolean diningInStatus;

    public MeasureRestaurant(@NotNull(message = "Created date should not be null") Date creationDateTime,
            @NotNull(message = "Max capacity should not be null") int maxPerTable,
            @NotNull(message = "Vaccination status should not be null") boolean vaccinationStatus,
            @NotNull(message = "Dining in status should not be null") boolean diningInStatus) {
        this.creationDateTime = creationDateTime;
        this.maxPerTable = maxPerTable;
        this.vaccinationStatus = vaccinationStatus;
        this.diningInStatus = diningInStatus;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public int getMaxPerTable() {
        return maxPerTable;
    }

    public void setMaxPerTable(int maxPerTable) {
        this.maxPerTable = maxPerTable;
    }

    public boolean isVaccinationStatus() {
        return vaccinationStatus;
    }

    public void setVaccinationStatus(boolean vaccinationStatus) {
        this.vaccinationStatus = vaccinationStatus;
    }

    public boolean isDiningInStatus() {
        return diningInStatus;
    }

    public void setDiningInStatus(boolean diningInStatus) {
        this.diningInStatus = diningInStatus;
    }

    
}
