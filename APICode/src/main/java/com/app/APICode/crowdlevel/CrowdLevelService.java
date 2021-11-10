package com.app.APICode.crowdlevel;

import java.util.List;

public interface CrowdLevelService {
    List<CrowdLevel> listAllCrowdLevels(String username);

    List<CrowdLevel> listCrowdLevelByEmployee(String username);

    CrowdLevel addCrowdLevel(Long restaurantID, CrowdLevel crowdLevel);

    CrowdLevel updateCrowdLevel(Long restaurantID, Long crowdLevelId, CrowdLevel newCrowdLevel);
}
