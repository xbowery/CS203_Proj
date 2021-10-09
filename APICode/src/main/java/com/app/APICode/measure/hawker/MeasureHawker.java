package com.app.APICode.measure.hawker;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity 
public class MeasureHawker {
    public MeasureHawker() {}
    
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

    @NotNull(message = "Created date should not be null")
    private Date creationDateTime;

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
