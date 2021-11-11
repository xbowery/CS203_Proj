package com.app.APICode.ctest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;
import com.app.APICode.restaurant.Restaurant;
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
        ctestService.setEmployees(employees);

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
        ctestService.setEmployees(employees);
        //Arrange
        User user = new User("pendingemployee2@test.com", "user6", "User", "six", "testing23456", false, "ROLE_USER");
        Employee employee = new Employee(user, "HR Manager");
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Ctest ctest = new Ctest("PCR",date,"Negative");

        when(employees.getEmployeeByUsername(user.getUsername())).thenReturn(employee);
        when(ctests.save(ctest)).thenReturn(ctest);

        //Act
        Ctest savedCtest = ctestService.saveCtestByUsername(user.getUsername(), ctest);

        //Assert
        assertNotNull(savedCtest);
        verify(employees).getEmployeeByUsername(user.getUsername());
        verify(ctests).save(ctest);
    } 
    
    @Test
    void updateCtest_ValidUsername_ReturnCtest() {
        ctestService.setEmployees(employees);
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

        Ctest ctest2 = ctest;
        ctest2.setResult("Positive");
        when(employees.getEmployeeByUsername(user.getUsername())).thenReturn(employee);
        when(ctests.findByIdAndEmployeeId(ctest.getId(),employee.getId())).thenReturn(Optional.of(ctest));
        when(ctests.save(ctest)).thenReturn(ctest2);
        //Act
        Ctest updatedctest = ctestService.updateCtestByCtestIdAndUsername(user.getUsername(), ctest.getId(), ctest2);
        
        //Assert
        assertNotNull(updatedctest);
        assertEquals("Positive", ctest.getResult());
        verify(employees).getEmployeeByUsername(user.getUsername());
        verify(ctests).findByIdAndEmployeeId(ctest.getId(), employee.getId());
        verify(ctests).save(ctest);
    }

    @Test
    void updateCtest_InvalidCtestId_ReturnException() {
        ctestService.setEmployees(employees);
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

        Ctest ctest2 = ctest;
        ctest2.setResult("Positive");
        Long CtestId = 1000L;
        when(employees.getEmployeeByUsername(user.getUsername())).thenReturn(employee);
       
    
        //Act
        CtestNotFoundException notFoundException = assertThrows(CtestNotFoundException.class, () -> {
            ctestService.updateCtestByCtestIdAndUsername(user.getUsername(), CtestId, ctest2);
        });
       
        //Assert
        assertEquals(notFoundException.getMessage(),"Could not find Covid Test " + CtestId);
        verify(employees).getEmployeeByUsername(user.getUsername());
    }

    @Test
    void deleteCtest_ValidCtestId_ReturnNull() {
        ctestService.setEmployees(employees);
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
        when(ctests.existsByIdAndEmployeeId(ctest.getId(), employee.getId())).thenReturn(true);

        //Act
        ctestService.deleteCtestByCtestIdAndUsername(user.getUsername(), ctest.getId());

        //Assert
        verify(ctests).deleteById(ctest.getId());
    }

    @Test
    void deleteCtest_InvalidCtestId_ReturnException() {
        ctestService.setEmployees(employees);
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

        Long CtestId = 1000L;
        when(employees.getEmployeeByUsername(user.getUsername())).thenReturn(employee);

        //Act
        CtestNotFoundException notFoundException = assertThrows(CtestNotFoundException.class, () -> {
            ctestService.deleteCtestByCtestIdAndUsername(user.getUsername(), CtestId);
        });
       
        //Assert
        assertEquals(notFoundException.getMessage(),"Could not find Covid Test " + CtestId);
        verify(employees).getEmployeeByUsername(user.getUsername());
    }

    @Test
    void getNextCtestDate_ValidUsername_ReturnDate() {
        ctestService.setEmployees(employees);
        
        //Arrange
        User user = new User("pendingemployee2@test.com", "user6", "User", "six", "testing23456", false, "ROLE_USER");
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        restaurant.setTestFrequency(7);
        Employee employee = new Employee(user, "HR Manager");
        employee.setRestaurant(restaurant);

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Ctest ctest = new Ctest("PCR",date,"Negative");
        ctest.setEmployee(employee);
        List<Ctest> allctests = new ArrayList<Ctest>();
        allctests.add(ctest);
        employee.setCtests(allctests);
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");

        when(employees.getEmployeeByUsername(any(String.class))).thenReturn(employee);
        
        //Act
        Date nextCtestDate = ctestService.getNextCtestByUsername(user.getUsername());

        //Assert
        assertNotNull(nextCtestDate);

    }

    @Test
    void getEmployeeCtest_ValidEmployee_ReturnList() {
        ctestService.setEmployees(employees);

        //Arrange
        User user = new User("pendingemployee2@test.com", "user6", "User", "six", "testing23456", false, "ROLE_USER");
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        restaurant.setTestFrequency(7);
        Employee employee = new Employee(user, "HR Manager");
        employee.setRestaurant(restaurant);

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Ctest ctest = new Ctest("PCR",date,"Negative");
        ctest.setEmployee(employee);
        List<Ctest> allctests = new ArrayList<Ctest>();
        allctests.add(ctest);
        employee.setCtests(allctests);
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");

        when(employees.getEmployeeByUsername(any(String.class))).thenReturn(employee);

        // Act
        List<Ctest> employeeCtest = ctestService.getAllCtestsByUsername(user.getUsername());

        // Arrange
        assertNotNull(employeeCtest);
    }
}
