package com.app.APICode.ctest;

import java.util.List;

import com.app.APICode.employee.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CtestServiceImpl implements CtestService {

    private CtestRepository ctests;

    @Autowired
    public CtestServiceImpl(CtestRepository ctests) {
        this.ctests = ctests;
    }

    @Override
    public List<Ctest> getAllCtestsByEmployee(Employee employee) {
        return ctests.findByEmployee(employee);
    }

    @Override
    public Ctest saveCtest(Ctest ctest) {
        return ctests.save(ctest);
    }

    @Override
    public Ctest updateCtestByCtestIdAndEmployeeId(Long ctestId, Long employeeId, Ctest newCtest) {
        return ctests.findByIdAndEmployeeId(ctestId, employeeId).map(ctest -> {
            ctest.setType(newCtest.getType());
            ctest.setDate(newCtest.getDate());
            ctest.setResult(newCtest.getResult());
            return ctests.save(ctest);
        }).orElseThrow(() -> new CtestNotFoundException(ctestId));
    }

    @Override
    public Ctest deleteCtestByCtestIdAndEmployeeId(Long ctestId, Long employeeId) {
        return ctests.findByIdAndEmployeeId(ctestId, employeeId).map(ctest -> {
            ctests.delete(ctest);
            return ctest;
        }).orElseThrow(() -> new CtestNotFoundException(ctestId));
    }
    
}
