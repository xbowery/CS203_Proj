package com.app.APICode.crowdlevel;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.validation.constraints.NotNull;

import com.app.APICode.restaurant.Restaurant;

@Entity
public class CrowdLevel {
    private @Id @GeneratedValue Long id;

    @NotNull(message = "Datetime cannot be null")
    private Date datetime;

    private String crowdLevel;

    @OneToOne
    @PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "restaurant_name", referencedColumnName = "name"),
        @PrimaryKeyJoinColumn(name = "restaurant_location", referencedColumnName = "location")    
    })
    private Restaurant restaurant;

    public CrowdLevel() {}

    
    public CrowdLevel(Date datetime, String crowdLevel) {
        this.datetime = datetime;
        this.crowdLevel = crowdLevel;
    }


    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel() {
        double utilization = (double)restaurant.getCurrentCapacity()/restaurant.getMaxCapacity();
        if (utilization <= 0.4) {
            this.crowdLevel = "Low";
        } else if (utilization <= 0.7)  {
            this.crowdLevel = "Medium";
        } else {
            this.crowdLevel = "High";
        }
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}
