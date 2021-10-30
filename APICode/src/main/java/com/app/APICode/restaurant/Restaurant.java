package com.app.APICode.restaurant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.app.APICode.crowdlevel.CrowdLevel;
import com.app.APICode.employee.Employee;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue
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

    private String currentCrowdLevel;

    @NotNull(message = "Max capacity should not be null")
    private int maxCapacity;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private List<Employee> employees;

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CrowdLevel> crowdLevel;

    public Restaurant() {
    }

    public Restaurant(String name, String location, String cuisine, String description, int maxCapacity) {
        this.name = name;
        this.location = location;
        this.cuisine = cuisine;
        this.description = description;
        this.maxCapacity = maxCapacity;
        crowdLevel = new ArrayList<>();
        this.currentCrowdLevel = "Low";
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Employee> getEmployees() {
        return this.employees;
    }

    public void setcurrentCrowdLevel(String currentCrowdLevel){
        this.currentCrowdLevel = currentCrowdLevel;
    }
    public String getcurrentCrowdLevel(){
        return this.currentCrowdLevel;
    }

    // public CrowdLevel getCrowdLevel() {
    //     return crowdLevel.getCrowdLevel();
    // }

    // public void setCrowdLevel(CrowdLevel crowdLevel) {
    //     this.crowdLevel = crowdLevel;
    // }

    // public void setCrowdLevel() {
    //     double utilization = (double)this.currentCapacity/this.maxCapacity;
    //     if (utilization <= 0.4) {
    //         this.crowdLevel = "Low";
    //     } else if (utilization <= 0.7)  {
    //         this.crowdLevel = "Medium";
    //     } else {
    //         this.crowdLevel = "High";
    //     }
    // }

}
