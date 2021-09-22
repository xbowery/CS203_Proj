package com.app.APICode.measure;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity 
public class MeasureHawker {
    @NotNull (message = "Created date should not be null")
    private @Temporal(TemporalType.TIMESTAMP)
    Date creationDateTime;

    @NotNull(message = "Max capacity should not be null")
    private int maxPerTable;

    @NotNull(message = "Vaccination status should not be null")
    private boolean vaccinationStatus;


    public MeasureHawker(Date creationDateTime, int maxPerTable, boolean vaccinationStatus) {
        this.creationDateTime = creationDateTime;
        this.maxPerTable = maxPerTable;
        this.vaccinationStatus = vaccinationStatus;
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

}
