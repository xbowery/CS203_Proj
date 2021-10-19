package com.app.APICode.crowdlevel;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.app.APICode.restaurant.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrowdLevelController {
    private CrowdLevelRepository crowdlevels;
    private RestaurantRepository restaurants;
    private CrowdLevelService crowdLevelService;

    public CrowdLevelController(CrowdLevelRepository crowdlevels, RestaurantRepository restaurants,
            CrowdLevelService crowdLevelService) {
        this.crowdlevels = crowdlevels;
        this.restaurants = restaurants;
        this.crowdLevelService = crowdLevelService;
    }

    @GetMapping("restaurant/crowdLevels")
    public List<CrowdLevel> getCrowdLevels(){
        return crowdLevelService.listCrowdLevels();
    }
    /**
     * Search for the crowd level of a restaurant with the given name and location 
     * 
     * @param name name of restaurant
     * @param location location of restaurant
     * @return crowd level of the restaurant
     */
    @GetMapping("/restaurant/{name}/{location}/crowdLevel")
    public CrowdLevel getCrowdLevelByRestaurant(@PathVariable String name, @PathVariable String location) {
        Restaurant restaurant = restaurants.findByNameAndLocation(name, location).orElse(null);
        return crowdlevels.findByRestaurant(restaurant).orElse(null);
    }

    // @GetMapping("/crowdLevel/{datetime}")
    // public Optional<CrowdLevel> getCrowdLevelByDatetime(@PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) Date datetime){
    //     return crowdlevels.findByDatetime(datetime);
    // }

    @PostMapping("/restaurant/{name}/{location}/crowdLevel")
    public CrowdLevel addCrowdLevel(@PathVariable String name, @PathVariable String location, @Valid @RequestBody CrowdLevel crowdLevel) {
        return restaurants.findByNameAndLocation(name, location).map(restaurant -> {
            crowdLevel.setRestaurant(restaurant);
            crowdLevel.setCrowdLevel();
            return crowdlevels.save(crowdLevel);
        }).orElseThrow(() -> new RestaurantNotFoundException(name, location));
    }

    @PutMapping("/restaurant/{name}/{location}/crowdLevel/{crowdLevelDateTime}")
    public CrowdLevel updateCrowdLevel(@PathVariable (value = "restaurantName") String name, @PathVariable (value = "restaurantLocation") String location,
    @PathVariable(value = "crowdLevelDateTime") @DateTimeFormat(iso = ISO.DATE_TIME) Date crowdLevelDateTime){
        return crowdlevels.findByDatetime(crowdLevelDateTime).map(crowdLevel -> {
            crowdLevel.setCrowdLevel();
            return crowdlevels.save(crowdLevel);
        }).orElseThrow(() -> new CrowdLevelNotFoundException(crowdLevelDateTime));
    }

}
