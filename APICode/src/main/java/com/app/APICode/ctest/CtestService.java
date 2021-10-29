package com.app.APICode.ctest;

import java.util.List;

import com.app.APICode.employee.Employee;

public interface CtestService {
    List<Ctest> getAllCtestsByUsername(String username);

    Ctest saveCtestByUsername(String username, Ctest ctest);

    Ctest updateCtestByCtestIdAndUsername(String username, Long ctestId, Ctest newCtest);

    Ctest deleteCtestByCtestIdAndUsername(String username, Long ctestId);

    Employee getEmployee(String username);
}
