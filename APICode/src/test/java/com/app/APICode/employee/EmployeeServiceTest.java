package com.app.APICode.employee;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.app.APICode.ctest.CtestRepository;
import com.app.APICode.restaurant.RestaurantRepository;
import com.app.APICode.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    
    // @Mock
    // private EmployeeRepository employees;

    @Mock
    private UserRepository users;

    @Mock
    private RestaurantRepository restaurants;

    @Mock
    private CtestRepository ctests;
}
