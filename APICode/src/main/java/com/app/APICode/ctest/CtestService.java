package com.app.APICode.ctest;

import java.util.List;

import com.app.APICode.employee.Employee;

public interface CtestService {
    List<Ctest> getAllCtestsByEmployee(Employee employee);

    Ctest saveCtest(Ctest ctest);

    Ctest updateCtestByCtestIdAndEmployeeId(Long ctestId, Long employeeId, Ctest newCtest);

    Ctest deleteCtestByCtestIdAndEmployeeId(Long ctestId, Long employeeId);
}
