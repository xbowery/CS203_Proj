package com.app.APICode.crowdlevel;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeNotFoundException;
import com.app.APICode.restaurant.*;
import com.app.APICode.user.User;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserRepository;
import com.app.APICode.crowdlevel.CrowdLevelNotFoundException;

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
    private UserRepository users;

    public CrowdLevelController(CrowdLevelRepository crowdlevels, RestaurantRepository restaurants, UserRepository users) {
        this.crowdlevels = crowdlevels;
        this.restaurants = restaurants;
        this.users = users;
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
    @GetMapping("/restaurants/{username}/crowdLevel")
    public List<CrowdLevel> getCrowdLevelByRestaurant(@PathVariable (value = "username") String username) {

        Optional<User> user = users.findByUsername(username);
        if(!user.isPresent()){
            throw new UserNotFoundException(username);
        }
        
        Employee employee = user.get().getEmployee();
        if(employee == null){
            throw new EmployeeNotFoundException(username);
        }

        Restaurant restaurant = employee.getRestaurant();
        if(restaurant == null){
            throw new RestaurantNotFoundException(username); 
        }

        List<CrowdLevel> crowdLevel = crowdlevels.findByRestaurant(restaurant);
        if(crowdLevel == null){
            throw new CrowdLevelNotFoundException(restaurant.getName());
        }
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

            restaurant.setCurrentCapacity(crowdLevel.getNoOfCustomers());
            restaurant.setcurrentCrowdLevel(crowdLevel.getLatestCrowd());
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
    @PutMapping("/restaurants/{id}/crowdLevel/{crowdLevelId}")
    public CrowdLevel updateCrowdLevel(@PathVariable Long id,
    @PathVariable Long crowdLevelId, @RequestBody CrowdLevel newCrowdLevel){
        return crowdlevels.findById(crowdLevelId) .map(crowdLevel -> {
            crowdLevel.setNoOfCustomers(newCrowdLevel.getNoOfCustomers());
            crowdLevel.setLatestCrowd();
            return crowdlevels.save(crowdLevel);
        }).orElseThrow(() -> new CrowdLevelNotFoundException(crowdLevelId));
    }

}


