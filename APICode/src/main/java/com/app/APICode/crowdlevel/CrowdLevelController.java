package com.app.APICode.crowdlevel;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import com.app.APICode.restaurant.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class CrowdLevelController {
    private CrowdLevelRepository crowdlevels;
    private RestaurantRepository restaurants;

    public CrowdLevelController(CrowdLevelRepository crowdlevels, RestaurantRepository restaurants) {
        this.crowdlevels = crowdlevels;
        this.restaurants = restaurants;
    }

    /**
     * Search for restaurant with the given name and location If there is no
     * restaurant with the given name and location, throw
     * RestaurantNotFoundException
     * 
     * @param name     name of restaurant
     * @param location location of restaurant
     * @return crowd level of the restaurant
     */
    @GetMapping("/restaurant/{name}/{location}/crowdLevel")
    public Optional<CrowdLevel> getCrowdLevelByRestaurant(@PathVariable (value = "restaurantName") String name, @PathVariable (value = "restaurantLocation") String location) {
        return crowdlevels.findByRestaurantNameAndRestaurantLocation(name, location);
    }

    @PostMapping("/restaurant/{name}/{location}/crowdLevel")
    public CrowdLevel addCrowdLevel(@PathVariable (value = "restaurantName") String name, @PathVariable (value = "restaurantLocation") String location, @Valid @RequestBody CrowdLevel crowdLevel) {
        return restaurants.findByNameAndLocation(name, location).map(restaurant -> {
            crowdLevel.setRestaurant(restaurant);
            return crowdlevels.save(crowdLevel);
        }).orElse(null);
    }

    @PutMapping("/restaurant/{name}/{location}/crowdLevel/{crowdLevelDateTime}")
    public CrowdLevel updateCrowdLevel(@PathVariable (value = "restaurantName") String name, @PathVariable (value = "restaurantLocation") String location,
    @PathVariable(value = "crowdLevelDateTime") Date crowdLevelDateTime){
        return crowdlevels.findByDatetime(crowdLevelDateTime).map(crowdLevel -> {
            Restaurant restaurant = crowdLevel.getRestaurant();
            crowdLevel.setCrowdLevel(restaurant);
            return crowdlevels.save(crowdLevel);
        }).orElse(null);
    }

}
