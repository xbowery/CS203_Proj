package com.app.APICode.ctest;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;
import com.app.APICode.restaurant.Restaurant;

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

    @Override
    public Date getNextCtestByUsername(String username){
        Employee employee = employees.getEmployeeByUsername(username);
        int testFrequency = employee.getRestaurant().getTestFrequency();
        Date latestTest = null;

        List<Ctest> ctestList = getAllCtestsByUsername(username);
        if(!ctestList.isEmpty()){
            latestTest = ctestList.get(0).getDate();
            for(Ctest ctest: getAllCtestsByUsername(username)){
                if(ctest.getDate().compareTo(latestTest) > 0){
                    latestTest = ctest.getDate();
                }
            }
        }else {
            return new java.sql.Date(Calendar.getInstance().getTime().getTime());
        }
        Calendar c = Calendar.getInstance();
        c.setTime(latestTest);
        c.add(Calendar.DATE, testFrequency);
        return  new Date(c.getTimeInMillis());
    }
}
