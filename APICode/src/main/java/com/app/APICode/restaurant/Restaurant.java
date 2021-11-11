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

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue
    @Schema(description = "Unique identifier of the Restaurant.", example = "1", required = true)
    private long id;

    @NotNull(message = "Restaurant name should not be null")
    @Schema(description = "Name of Restaurant.", example = "Koufu", required = true)
    private String name;

    @NotNull(message = "Location should not be null")
    @Schema(description = "Location of Restaurant.", example = "SMU", required = true)
    private String location;

    @NotNull(message = "Cuisine should not be null")
    @Schema(description = "Cuisine of Restaurant.", example = "Asian", required = true)
    private String cuisine;

    @NotNull(message = "Description should not be null")
    @Schema(description = "Description of Restaurant.", example = "Lovely food", required = true)
    private String description;

    @Schema(description = "Current number of people in Restaurant.", example = "50")
    private int currentCapacity;

    @Schema(description = "Current crowd level of people in restaurant.", example = "Low")
    private String currentCrowdLevel;

    @Schema(description = "Frequency of test required for restaurant employees", example = "7")
    private int testFrequency;

    @NotNull(message = "Max capacity should not be null")
    @Schema(description = "Seating capacity in Restaurant.", example = "100")
    private int maxCapacity;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.PERSIST)
    @Schema(description = "List of Employees working at the restaurant.")
    @JsonManagedReference
    private List<Employee> employees;

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true, cascade = CascadeType.ALL)
    @Schema(description = "List of Crowd Level recording in restaurant.")
    @JsonManagedReference
    private List<CrowdLevel> crowdLevel;

    @Schema(description = "String containing the url to the cover image of the restaurant")
    private String imageURL;

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
        this.testFrequency = 7;
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

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void setcurrentCrowdLevel(String currentCrowdLevel){
        this.currentCrowdLevel = currentCrowdLevel;
    }

    public String getcurrentCrowdLevel() {
        return this.currentCrowdLevel;
    }

    public int getTestFrequency() {
        return this.testFrequency;
    }

    public void setTestFrequency(int testFrequency) {
        this.testFrequency = testFrequency;
    }

    public String getImageUrl() {
        return this.imageURL;
    }

    public void setImageUrl(String imageURl) {
        this.imageURL = imageURl;
    }

    public void setCrowdLevel(List<CrowdLevel> crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    public List<CrowdLevel> getCrowdLevel() {
        return this.crowdLevel;
    }
}
