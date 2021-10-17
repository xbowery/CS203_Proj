package com.app.APICode.employee;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.app.APICode.ctest.Ctest;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.UUIDGenerator.class, property="@Id")
@Entity
public class Employee {
    private static final long serialVersionUID = 1L;

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "employeeId")
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "employee", orphanRemoval = true, cascade=CascadeType.ALL)
    private List<Ctest> ctests;


    public void setUser(User user){
        this.user = user;
    }

    public User getUser (){
        return user;
    }

    public void setRestaurant(Restaurant restaurant){
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant(){
        return restaurant;
    }

    public Employee(User user){
        this.user = user;
        ctests = new ArrayList<>();
    }

    public Employee(){
        super();
    }
}
