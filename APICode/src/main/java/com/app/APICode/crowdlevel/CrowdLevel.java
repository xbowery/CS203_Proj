package com.app.APICode.crowdlevel;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.app.APICode.restaurant.Restaurant;

@Entity
public class CrowdLevel {
    private @Id @GeneratedValue Long id;

    // private Crowd latestCrowd;

    // @OneToMany(mappedBy = "restaurant", orphanRemoval = true, cascade = CascadeType.ALL)
    // private List<Crowd> historialCrowdLevel;

    private Date datetime;

    private String latestCrowd;

    private int noOfCustomers;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    public CrowdLevel() {
    }

    // public CrowdLevel(Crowd latestCrowd, List<Crowd> historialCrowdLevel, Restaurant restaurant) {
    //     this.latestCrowd = latestCrowd;
    //     historialCrowdLevel = new ArrayList<>();
    //     this.restaurant = restaurant;
    // }

    // public Crowd getLatestCrowd() {
    //     return latestCrowd;
    // }

    // public void setLatestCrowd() {
    //     latestCrowd.setCrowdLevel(restaurant.getMaxCapacity());
    // }

    // public List<Crowd> getHistorialCrowdLevel() {
    //     return historialCrowdLevel;
    // }

    // public void setHistorialCrowdLevel(List<Crowd> historialCrowdLevel) {
    //     this.historialCrowdLevel = historialCrowdLevel;
    // }

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
        double utilization = (double) getNoOfCustomers()/ restaurant.getMaxCapacity();
        if (utilization <= 0.4) {
            this.latestCrowd = "Low";
        } else if (utilization <= 0.7)  {
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

}
