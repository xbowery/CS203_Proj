package com.app.APICode.employee;

import com.app.APICode.user.*;
import com.app.APICode.restaurant.*;
import com.app.APICode.ctest.*;

import java.util.List;
import java.util.Collection;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
