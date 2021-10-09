package com.app.APICode.employee;

import java.security.Principal;
import java.util.List;
import java.util.ArrayList;

import com.app.APICode.user.UserService;
import com.app.APICode.user.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private UserService userService;
    private EmployeeService employeeService;

    public EmployeeController(UserService userService, EmployeeService employeeService) {
        this.userService = userService;
        this.employeeService = employeeService;
    }

    @GetMapping("/employees") 
    public List<User> getEmployees(Principal principal) {
        Long principal_id = userService.getUserIdByUsername(principal.getName());

        List<Employee> employeeList = employeeService.listEmployees(principal_id);

        List<User> userList = new ArrayList<>();

        for (Employee employee : employeeList) {
            Long user_id = employee.getId();

            User user = userService.getUserById(user_id);

            userList.add(user);
        }

        return userList;
    }
}
