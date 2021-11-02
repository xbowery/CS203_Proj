package com.app.APICode.ctest;

import java.util.List;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CtestServiceImpl implements CtestService {

    private CtestRepository ctests;
    private EmployeeService employees;

    @Autowired
    public CtestServiceImpl(CtestRepository ctests, EmployeeService employees) {
        this.ctests = ctests;
        this.employees = employees;
    }

    @Override
    public List<Ctest> getAllCtestsByUsername(String username) {
        Employee employee = employees.getEmployeeByUsername(username);
        return ctests.findByEmployee(employee);
    }

    @Override
    public Ctest saveCtestByUsername(String username, Ctest ctest) {
        Employee employee = employees.getEmployeeByUsername(username);
        ctest.setEmployee(employee);
        return ctests.save(ctest);
    }

    @Override
    public Ctest updateCtestByCtestIdAndUsername(String username, Long ctestId, Ctest newCtest) {
        Employee employee = employees.getEmployeeByUsername(username);
        return ctests.findByIdAndEmployeeId(ctestId, employee.getId()).map(ctest -> {
            ctest.setType(newCtest.getType());
            ctest.setDate(newCtest.getDate());
            ctest.setResult(newCtest.getResult());
            return ctests.save(ctest);
        }).orElseThrow(() -> new CtestNotFoundException(ctestId));
    }

    @Override
    public Ctest deleteCtestByCtestIdAndUsername(String username, Long ctestId) {
        Employee employee = employees.getEmployeeByUsername(username);
        return ctests.findByIdAndEmployeeId(ctestId, employee.getId()).map(ctest -> {
            ctests.delete(ctest);
            return ctest;
        }).orElseThrow(() -> new CtestNotFoundException(ctestId));
    }
}
