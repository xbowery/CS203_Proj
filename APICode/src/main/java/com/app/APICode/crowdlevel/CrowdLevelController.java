package com.app.APICode.crowdlevel;

import java.util.List;

import javax.validation.Valid;

import com.app.APICode.restaurant.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrowdLevelController {
    private CrowdLevelRepository crowdlevels;
    private RestaurantRepository restaurants;

    public CrowdLevelController(CrowdLevelRepository crowdlevels, RestaurantRepository restaurants) {
        this.crowdlevels = crowdlevels;
        this.restaurants = restaurants;
    }

    @GetMapping("restaurant/crowdLevels")
    public List<CrowdLevel> getCrowdLevels(){
        return crowdlevels.findAll();
    }
    /**
    //  * Search for the crowd level of a restaurant with the given name and location 
    //  * 
    //  * @param name name of restaurant
    //  * @param location location of restaurant
    //  * @return crowd level of the restaurant
     */
    @GetMapping("/restaurant/{id}/crowdLevel")
    public CrowdLevel getCrowdLevelByRestaurant(@PathVariable Long id) {
        Restaurant restaurant = restaurants.findById(id).orElse(null);
        return crowdlevels.findByRestaurant(restaurant).orElse(null);
    }

    /**
     * Add a new crowd level with POST request to "/restaurant/{id}/crowdLevel"
     * 
     * @param id id of restaurant
     * @return the newly added CrowdLevel object
     */
    @PostMapping("/restaurant/{id}//crowdLevel")
    public CrowdLevel addCrowdLevel(@PathVariable Long id, @Valid @RequestBody CrowdLevel crowdLevel) {
        return restaurants.findById(id).map(restaurant -> {
            crowdLevel.setRestaurant(restaurant);
            crowdLevel.setLatestCrowd();
            return crowdlevels.save(crowdLevel);
        }).orElse(null);
    }

    // @PutMapping("/restaurant/{id}/crowdLevel/{crowdLevelDateTime}")
    // public CrowdLevel updateCrowdLevel(@PathVariable Long id,
    // @PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) Date crowdLevelDateTime, @RequestBody CrowdLevel newCrowdLevel){
    //     return crowdlevels.findByDatetime(crowdLevelDateTime).map(crowdLevel -> {
    //         crowdLevel.setLatestCrowd(newCrowdLevel.getLatestCrowd());
    //         return crowdlevels.save(crowdLevel);
    //     }).orElseThrow(() -> new CrowdLevelNotFoundException(crowdLevelDateTime));
    // }

}
