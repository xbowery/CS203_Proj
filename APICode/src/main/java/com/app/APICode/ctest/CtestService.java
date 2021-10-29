package com.app.APICode.ctest;

import java.util.List;

public interface CtestService {
    List<Ctest> getAllCtestsByUsername(String username);

    Ctest saveCtestByUsername(String username, Ctest ctest);

    Ctest updateCtestByCtestIdAndUsername(String username, Long ctestId, Ctest newCtest);

    Ctest deleteCtestByCtestIdAndUsername(String username, Long ctestId);
}
