package com.app.APICode.crowdlevel;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Crowd {

    private @Id @GeneratedValue Long id;

    private Date datetime;

    private int noOfCustomers;

    private String crowdLevel;

    @ManyToOne
    @JoinColumn(name = "CrowdLevelId")
    private CrowdLevel crowdlevel;

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getNoOfCustomers() {
        return noOfCustomers;
    }

    public void setNoOfCustomers(int noOfCustomers) {
        this.noOfCustomers = noOfCustomers;
    }

    public String getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(int maxCapacity) {
        double utilization = (double) getNoOfCustomers()/ maxCapacity;
        if (utilization <= 0.4) {
            this.crowdLevel = "Low";
        } else if (utilization <= 0.7)  {
            this.crowdLevel = "Medium";
        } else {
            this.crowdLevel = "High";
        }
    }

    



}
