package com.app.APICode.ctest;

import java.util.List;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeNotFoundException;
import com.app.APICode.user.User;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CtestServiceImpl implements CtestService {

    private CtestRepository ctests;
    private UserService users;

    @Autowired
    public CtestServiceImpl(CtestRepository ctests, UserService users) {
        this.ctests = ctests;
        this.users = users;
    }

    @Override
    public List<Ctest> getAllCtestsByUsername(String username) {
        Employee employee = getEmployee(username);
        return ctests.findByEmployee(employee);
    }

    @Override
    public Ctest saveCtestByUsername(String username, Ctest ctest) {
        Employee employee = getEmployee(username);
        ctest.setEmployee(employee);
        return ctests.save(ctest);
    }

    @Override
    public Ctest updateCtestByCtestIdAndUsername(String username, Long ctestId, Ctest newCtest) {
        Employee employee = getEmployee(username);
        return ctests.findByIdAndEmployeeId(ctestId, employee.getId()).map(ctest -> {
            ctest.setType(newCtest.getType());
            ctest.setDate(newCtest.getDate());
            ctest.setResult(newCtest.getResult());
            return ctests.save(ctest);
        }).orElseThrow(() -> new CtestNotFoundException(ctestId));
    }

    @Override
    public Ctest deleteCtestByCtestIdAndUsername(String username, Long ctestId) {
        Employee employee = getEmployee(username);
        return ctests.findByIdAndEmployeeId(ctestId, employee.getId()).map(ctest -> {
            ctests.delete(ctest);
            return ctest;
        }).orElseThrow(() -> new CtestNotFoundException(ctestId));
    }

    public Employee getEmployee(String username) {
        User user = users.getUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }

        Employee employee = user.getEmployee();
        if (employee == null) {
            throw new EmployeeNotFoundException(username);
        }
        return employee;
    }
}
