package com.app.APICode.crowdlevel;

import java.util.List;

import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserForbiddenException;
import com.app.APICode.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public List<CrowdLevel> listCrowdLevelByEmployee(String username) {
        User user = userService.getUserByUsername(username);

        if (!(StringUtils.collectionToCommaDelimitedString(user.getAuthorities()).split("_")[1]).equals("BUSINESS")) {
            throw new UserForbiddenException("You are forbidden from processing this request.");
        }

        Restaurant restaurant = user.getEmployee().getRestaurant();

        List<CrowdLevel> crowdLevel = crowdlevels.findByRestaurant(restaurant);
        if(crowdLevel.size() == 0){
            throw new CrowdLevelNotFoundException(restaurant.getName());
        }

        return crowdlevels.findByRestaurant(restaurant);
    }

    @Override
    public CrowdLevel addCrowdLevel(String username, CrowdLevel crowdLevel) {

        Restaurant restaurant = restaurantService.getRestaurantByUsername(username);
        Long restaurantID = restaurant.getId();

        crowdLevel.setRestaurant(restaurant);
        crowdLevel.setLatestCrowd();

        restaurant.setCurrentCapacity(crowdLevel.getNoOfCustomers());
        restaurant.setcurrentCrowdLevel(crowdLevel.getLatestCrowd());
        restaurantService.updateRestaurant(restaurantID, restaurant);
        return crowdlevels.save(crowdLevel);
    }
}
