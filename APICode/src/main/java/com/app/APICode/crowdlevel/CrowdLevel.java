package com.app.APICode.crowdlevel;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.app.APICode.restaurant.Restaurant;
import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class CrowdLevel {
    @Schema(description = "Unique identifier of the Crowd Level Information.", example = "1", required = true)
    private @Id @GeneratedValue Long id;

    @NotNull(message = "datetime cannot be null")
    @Schema(description = "DateTime of Crowd Level Entry.", example = "1", required = true)
    private Date datetime;

    @NotNull(message = "latest crowd cannot be null")
    @Schema(description = "Likert Scale of Current Crowd Level.", example = "Medium", required = true)
    private String latestCrowd;

    @NotNull(message = "number of customers cannot be null")
    @Schema(description = "Number of customers in the Restaurants.", example = "50", required = true)
    private int noOfCustomers;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    @Schema(description = "Restaurant that the Crowd Level belong to.", required = true)
    @JsonBackReference 
    private Restaurant restaurant;

    public CrowdLevel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getLatestCrowd() {
        return latestCrowd;
    }

    public void setLatestCrowd() {
        double utilization = (double) getNoOfCustomers() / restaurant.getMaxCapacity();
        if (utilization <= 0.4) {
            this.latestCrowd = "Low";
        } else if (utilization <= 0.7) {
            this.latestCrowd = "Medium";
        } else {
            this.latestCrowd = "High";
        }
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public CrowdLevel(Date datetime, String latestCrowd, int noOfCustomers, Restaurant restaurant) {
        this.datetime = datetime;
        this.latestCrowd = latestCrowd;
        this.noOfCustomers = noOfCustomers;
        this.restaurant = restaurant;
    }

}
