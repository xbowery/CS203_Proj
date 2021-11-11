package com.app.APICode.crowdlevel;

import java.util.List;

public interface CrowdLevelService {

    List<CrowdLevel> listCrowdLevelByEmployee(String username);

    CrowdLevel addCrowdLevel(String username, CrowdLevel crowdLevel);
}
