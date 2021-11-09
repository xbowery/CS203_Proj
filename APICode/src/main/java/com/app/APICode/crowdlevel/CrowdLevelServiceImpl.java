package com.app.APICode.crowdlevel;

import java.util.List;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeNotFoundException;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantNotFoundException;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrowdLevelServiceImpl implements CrowdLevelService {

    private CrowdLevelRepository crowdlevels;

    private RestaurantService restaurantService;

    private UserService userService;

    @Autowired
    public CrowdLevelServiceImpl(CrowdLevelRepository crowdlevels, RestaurantService restaurantService, UserService userService) {
        this.crowdlevels = crowdlevels;
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    @Override
    public List<CrowdLevel> listAllCrowdLevels() {
        return crowdlevels.findAll();
    }

    @Override
    public List<CrowdLevel> listCrowdLevelByEmployee(String username) {
        User user = userService.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException(username);
        }

        Employee employee = user.getEmployee();

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

    @Override
    public CrowdLevel addCrowdLevel(Long restaurantID, CrowdLevel crowdLevel) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantID);

        if (restaurant == null) {
            throw new RestaurantNotFoundException(restaurantID); 
        }

        crowdLevel.setRestaurant(restaurant);
        crowdLevel.setLatestCrowd();

        restaurant.setCurrentCapacity(crowdLevel.getNoOfCustomers());
        restaurant.setcurrentCrowdLevel(crowdLevel.getLatestCrowd());
        restaurantService.updateRestaurant(restaurantID, restaurant);
        return crowdlevels.save(crowdLevel);
    }

    @Override
    public CrowdLevel updateCrowdLevel(Long restaurantID, Long crowdLevelId, CrowdLevel newCrowdLevel) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantID);

        if (restaurant == null) {
            throw new RestaurantNotFoundException(restaurantID);
        }

        CrowdLevel crowdLevel = crowdlevels.findById(crowdLevelId).orElse(null);

        if (crowdLevel == null) {
            throw new CrowdLevelNotFoundException(crowdLevelId);
        }

        restaurant.setCurrentCapacity(newCrowdLevel.getNoOfCustomers());
        restaurant.setcurrentCrowdLevel(newCrowdLevel.getLatestCrowd());
        restaurantService.updateRestaurant(restaurantID, restaurant);

        crowdLevel.setNoOfCustomers(newCrowdLevel.getNoOfCustomers());
        crowdLevel.setLatestCrowd();
        return crowdlevels.save(crowdLevel);
    }

}
