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

@Entity
public class Employee {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JsonBackReference 
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "employee", orphanRemoval = true, cascade = CascadeType.ALL)
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

    public Long getId(){
        return id;
    }
}
