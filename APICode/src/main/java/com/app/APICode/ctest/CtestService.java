package com.app.APICode.ctest;

import java.sql.Date;
import java.util.List;

public interface CtestService {
    List<Ctest> getAllCtestsByUsername(String username);

    Ctest saveCtestByUsername(String username, Ctest ctest);

    Ctest updateCtestByCtestIdAndUsername(String username, Long ctestId, Ctest newCtest);

    void deleteCtestByCtestIdAndUsername(String username, Long ctestId);

    Date getNextCtestByUsername(String username);

    List<Ctest> getAllEmployeesCtest(String username);
}
