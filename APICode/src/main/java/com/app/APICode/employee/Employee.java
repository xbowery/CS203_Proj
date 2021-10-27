package com.app.APICode.employee;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.app.APICode.ctest.Ctest;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class Employee {

    @Id
    @Schema(description = "Unique identifier of the Employee. Shared Key with User", example = "1", required = true)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    // @JsonIgnore
    @Schema(description = "User Details of Employee", required = true)
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "restaurantId")
    @Schema(description = "Restaurant Employee is working at", required = true)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "employee", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    @Schema(description = "List of Covid Testing records")
    private List<Ctest> ctests;

    public Employee() {
    }

    public Employee(User user) {
        this.user = user;
        ctests = new ArrayList<>();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Long getId() {
        return id;
    }
}
