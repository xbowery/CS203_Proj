package com.app.APICode.crowdlevel;

import java.util.List;

import com.app.APICode.restaurant.Restaurant;

public interface CrowdLevelService {
    List<CrowdLevel> listCrowdLevels();
    CrowdLevel getCrowdLevel(Restaurant restaurant);
    // CrowdLevel updateCrowdLevel();
}
