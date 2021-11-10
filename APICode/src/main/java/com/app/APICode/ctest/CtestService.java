package com.app.APICode.ctest;

import java.sql.Date;
import java.util.List;

public interface CtestService {
    List<Ctest> getAllCtestsByUsername(String username);

    Ctest saveCtestByUsername(String username, Ctest ctest);

    Ctest updateCtestByCtestIdAndUsername(String username, Long ctestId, Ctest newCtest);

    Ctest deleteCtestByCtestIdAndUsername(String username, Long ctestId);

    Date getNextCtestByUsername(String username);

    /**
     * Gets all {@link Ctest} for employees working in the business with the
     * specified business's owner "username"
     * 
     * @param username
     * @return
     */
    List<Ctest> getAllEmployeesCtest(String username);
}
