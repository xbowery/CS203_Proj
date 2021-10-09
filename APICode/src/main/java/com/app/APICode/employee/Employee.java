package com.app.APICode.employee;

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

@Entity
public class Employee extends User {
    private static final long serialVersionUID = 1L;

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "employee_id")
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "employee", orphanRemoval = true, cascade=CascadeType.ALL)
    private List<Ctest> ctests;
}
