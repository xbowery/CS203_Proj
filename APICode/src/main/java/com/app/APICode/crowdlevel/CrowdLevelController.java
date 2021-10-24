package com.app.APICode.crowdlevel;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.app.APICode.restaurant.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrowdLevelController {
    private CrowdLevelRepository crowdlevels;
    private RestaurantRepository restaurants;

    public CrowdLevelController(CrowdLevelRepository crowdlevels, RestaurantRepository restaurants) {
        this.crowdlevels = crowdlevels;
        this.restaurants = restaurants;
    }

        /**
     * List the latest crowd levels by datetime and restaurant in the system
     * @return list of latest crowd levels
     */
    @GetMapping("restaurants/crowdLevels")
    public List<CrowdLevel> getCrowdLevels(){
        return crowdlevels.findAll();
    }

    /**
     * Search for the crowd level of a restaurant with the given restaurant id
     * 
     * @param id restaurant id
     * @return crowd level of restaurant
     */
    @GetMapping("/restaurants/{id}/crowdLevel")
    public List<CrowdLevel> getCrowdLevelByRestaurant(@PathVariable Long id) {
        Restaurant restaurant = restaurants.findById(id).orElse(null);
        return crowdlevels.findByRestaurant(restaurant);
    }

    /**
     * Add a new crowd level with POST request to "/restaurant/{id}/crowdLevel"
     * 
     * @param id restaurant id
     * @return the newly added CrowdLevel object
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restaurants/{id}//crowdLevel")
    public CrowdLevel addCrowdLevel(@PathVariable Long id, @Valid @RequestBody CrowdLevel crowdLevel) {
        return restaurants.findById(id).map(restaurant -> {
            crowdLevel.setRestaurant(restaurant);
            crowdLevel.setLatestCrowd();
            return crowdlevels.save(crowdLevel);
        }).orElse(null);
    }

    /**
     * Update crowd level
     * If there is no crowd level with the given datetime, throw CrowdLevelNotFoundException
     * @param crowdLevelDateTime the datetime of the measure
     * @param newCrowdLevel a CrowdLevel object containing the new crowd level to be updated
     * @return the updated CrowdLevel object
     */
    @PutMapping("/restaurants/{id}/crowdLevel/{crowdLevelDateTime}")
    public CrowdLevel updateCrowdLevel(@PathVariable Long id,
    @PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) Date crowdLevelDateTime, @RequestBody CrowdLevel newCrowdLevel){
        return crowdlevels.findByDatetime(crowdLevelDateTime).map(crowdLevel -> {
            crowdLevel.setNoOfCustomers(newCrowdLevel.getNoOfCustomers());
            crowdLevel.setLatestCrowd();
            return crowdlevels.save(crowdLevel);
        }).orElseThrow(() -> new CrowdLevelNotFoundException(crowdLevelDateTime));
    }

}


