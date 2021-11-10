package com.app.APICode.ctest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;
import com.app.APICode.user.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class CtestServiceTest {
    @Mock 
    private CtestRepository ctests;

    @Mock
    private EmployeeService employees;

    @InjectMocks
    private CtestServiceImpl ctestService;

    @Test
    void getAllCtests_validUsername_ReturnCtests() {
        //Arrange
        User user = new User("pendingemployee2@test.com", "user6", "User", "six", "testing23456", false, "ROLE_USER");
        Employee employee = new Employee(user, "HR Manager");

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Ctest ctest = new Ctest("PCR",date,"Negative");
        ctest.setEmployee(employee);
        List<Ctest> allctests = new ArrayList<Ctest>();
        allctests.add(ctest);

        employee.setCtests(allctests);
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");

        when(employees.getEmployeeByUsername(user.getUsername())).thenReturn(employee);
        when(ctests.findByEmployee(employee)).thenReturn(allctests);
        //Act
        List<Ctest> ctestsList = ctestService.getAllCtestsByUsername(user.getUsername());

        //Assert
        assertEquals(1,ctestsList.size());
        verify(employees).getEmployeeByUsername(user.getUsername());
        verify(ctests).findByEmployee(employee);
    }

    @Test
    void saveCtest_ValidUsername_ReturnCtest() {
        //Arrange
        User user = new User("pendingemployee2@test.com", "user6", "User", "six", "testing23456", false, "ROLE_USER");
        Employee employee = new Employee(user, "HR Manager");
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Ctest ctest = new Ctest("PCR",date,"Negative");
        // List<Ctest> allctests = new ArrayList<Ctest>();
        // allctests.add(ctest);

        when(employees.getEmployeeByUsername(user.getUsername())).thenReturn(employee);
        when(ctests.save(ctest)).thenReturn(ctest);

        //Act
        Ctest savedCtest = ctestService.saveCtestByUsername(user.getUsername(), ctest);

        //Assert
        assertNotNull(savedCtest);
        verify(employees).getEmployeeByUsername(user.getUsername());
        verify(ctests).save(ctest);

    }   

}
