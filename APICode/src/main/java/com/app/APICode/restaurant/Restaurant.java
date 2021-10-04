package com.app.APICode.restaurant;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.app.APICode.employee.*;
import java.util.*;

@Entity
public class Restaurant {

    @Id @GeneratedValue
    private long id;

    @NotNull(message = "Restaurant name should not be null")
    private String name;

    @NotNull(message = "Location should not be null")
    private String location;

    @NotNull(message = "Cuisine should not be null")
    private String cuisine;

    @NotNull(message = "Description should not be null")
    private String description;

    private int currentCapacity;

    @NotNull(message = "Max capacity should not be null")
    private int maxCapacity;

    private String crowdLevel;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.PERSIST)
    private List<Employee> employees;

    public Restaurant(String name, String location, String cuisine, String description, int maxCapacity) {
        this.name = name;
        this.location = location;
        this.cuisine = cuisine;
        this.description = description;
        this.maxCapacity = maxCapacity;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCuisine() {
        return this.cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getCurrentCapacity() {
        return this.currentCapacity;
    }

    public void setCurrentCapacity(int capacity) {
        this.currentCapacity = capacity;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getCrowdLevel() {
        return this.crowdLevel;
    }

    public void setCrowdLevel() {
        double utilization = (double)this.currentCapacity/this.maxCapacity;
        if (utilization <= 0.4) {
            this.crowdLevel = "Low";
        } else if (utilization >= 0.4 && utilization <= 0.6)  {
            this.crowdLevel = "Medium";
        } else {
            this.crowdLevel = "High";
        }
    }

    public Restaurant() {}
}

