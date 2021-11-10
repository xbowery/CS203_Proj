package com.app.APICode.ctest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeForbiddenException;
import com.app.APICode.employee.EmployeeNotFoundException;
import com.app.APICode.employee.EmployeeService;
import com.app.APICode.restaurant.Restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CtestServiceImpl implements CtestService {

    private CtestRepository ctests;
    private EmployeeService employees;

    @Autowired
    public CtestServiceImpl(CtestRepository ctests) {
        this.ctests = ctests;
    }

    @Autowired
    public void setEmployees(EmployeeService employees) {
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

    @Override
    public Date getNextCtestByUsername(String username) {
        Employee employee = employees.getEmployeeByUsername(username);
        int testFrequency = employee.getRestaurant().getTestFrequency();
        Date latestTest = null;

        List<Ctest> ctestList = getAllCtestsByUsername(username);
        if (!ctestList.isEmpty()) {
            latestTest = ctestList.get(0).getDate();
            for(Ctest ctest: getAllCtestsByUsername(username)){
                if(ctest.getDate().compareTo(latestTest) > 0){
                    latestTest = ctest.getDate();
                }
            }
        } else {
            return new java.sql.Date(Calendar.getInstance().getTime().getTime());
        }
        Calendar c = Calendar.getInstance();
        c.setTime(latestTest);
        c.add(Calendar.DATE, testFrequency);
        return new Date(c.getTimeInMillis());
    }

    @Override
    public List<Ctest> getAllEmployeesCtest(String username) {
        List<Ctest> allCtests = new ArrayList<Ctest>();

        Employee owner = employees.getEmployeeByUsername(username);
        if (owner == null) {
            throw new EmployeeNotFoundException(username);
        } else if (!(StringUtils.collectionToCommaDelimitedString(owner.getUser().getAuthorities()).split("_")[1].equals("BUSINESS"))) {
            throw new EmployeeForbiddenException("You are forbidden from processing this request.");
        }

        List<Employee> employees = owner.getRestaurant().getEmployees();
        for (Employee e : employees) {
            if(e.getCtests().size() > 0){
                allCtests.add(e.getCtests().get(e.getCtests().size() - 1));
            }
        }
        return allCtests;
    }
}
