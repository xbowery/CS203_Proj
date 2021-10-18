package com.app.APICode.crowdlevel;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.app.APICode.restaurant.Restaurant;

@Entity
public class CrowdLevel {
    private @Id @GeneratedValue Long id;

    @NotNull(message = "Datetime cannot be null")
    private Date datetime;

    @NotNull(message = "Crowd level cannot be null")
    private String crowdLevel;

    @OneToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    public CrowdLevel() {}

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(Restaurant restaurant) {
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
