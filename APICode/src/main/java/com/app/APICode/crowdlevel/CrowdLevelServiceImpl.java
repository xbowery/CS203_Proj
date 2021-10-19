package com.app.APICode.crowdlevel;


import java.util.Date;
import java.util.List;

import com.app.APICode.restaurant.Restaurant;

import org.springframework.stereotype.Service;

@Service
public class CrowdLevelServiceImpl implements CrowdLevelService{
    private CrowdLevelRepository crowdlevels; 

    //for testing
    public CrowdLevelServiceImpl(CrowdLevelRepository crowdlevels){
        this.crowdlevels = crowdlevels;
        crowdlevels.save(new CrowdLevel(new Date(), "Medium"));
    }

    public List<CrowdLevel> listCrowdLevels(){
        return crowdlevels.findAll();
    }

    public CrowdLevel getCrowdLevel(Restaurant restaurant){
        return crowdlevels.findByRestaurant(restaurant).orElse(null);
    }

    // public CrowdLevel updateCrowdLevel(){

    // }
}
