package com.app.APICode.restaurant;

import java.util.List;
import java.util.stream.Collectors;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;
import com.app.APICode.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepository restaurants;
    private EmployeeService employees;

    // for testing purposes
    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurants) {
        this.restaurants = restaurants;
        //mock data
        Restaurant astons = new Restaurant("Astons", "Cathay", "Western", "Western steakhouse", 50);
        Restaurant dianXiaoEr = new Restaurant("Dian Xiao Er", "Hillion", "Chinese", 
        "Chinese dining experience in an ancient Inn", 50);
        Restaurant mcDonalds = new Restaurant("McDonalds","Raffles City", "Western", "Fast food chain",50);
        Restaurant saizeriya = new Restaurant("Saizeriya","Hillion","Italian","Italian Restaurant",50);
        Restaurant poulet = new Restaurant("Poulet","ION Orchard","French","Modern French restaurant",50);
        Restaurant jumbo = new Restaurant("Jumbo Seafood","Jewel Changi Airport","Seafood","Seafood restaurant",100);
        Restaurant sakaesushi = new Restaurant("Sakae Sushi","Junction 8","Japanese","Japanese restaurant",50);

        astons.setImageUrl("restaurant.jpg");
        dianXiaoEr.setImageUrl("dianXiaoEr.jpg");
        mcDonalds.setImageUrl("mcdonalds.jpg");
        saizeriya.setImageUrl("saizeriya.jpg");
        poulet.setImageUrl("poulet.jpg");
        jumbo.setImageUrl("jumbo.jpg");
        sakaesushi.setImageUrl("sakaesushi.jpg");
        
        restaurants.save(astons);
        restaurants.save(dianXiaoEr);
        restaurants.save(mcDonalds);
        restaurants.save(saizeriya);
        restaurants.save(poulet);
        restaurants.save(jumbo);
        restaurants.save(sakaesushi);
    }

    // To break circular dependency
    @Autowired
    public void setEmployees(EmployeeService employees) {
        this.employees = employees;
    }

    public EmployeeService getEmployees() {
        return this.employees;
    }

    @Override

    public List<RestaurantDTO> listRestaurants() {
        List<Restaurant> restaurantsList = restaurants.findAll();
        return restaurantsList.stream().map(this::convertToRestaurantDTO).collect(Collectors.toList());
    }

    private RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        return RestaurantDTO.convertToRestaurantDTO(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        Restaurant restaurant = restaurants.findById(id).orElse(null);
        if (restaurant == null)
            throw new RestaurantNotFoundException(id);
        return restaurant;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        Restaurant duplicate = restaurants.findByNameAndLocation(restaurant.getName(), restaurant.getLocation())
                .orElse(null);
        if (duplicate != null)
            throw new RestaurantDuplicateException(duplicate.getName(), duplicate.getLocation());
        return restaurants.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(long id, Restaurant updatedRestaurant) {
        Restaurant restaurant = restaurants.findById(id).orElse(null);
        if (restaurant == null) 
            throw new RestaurantNotFoundException(updatedRestaurant.getId());
        
        restaurant.setName(updatedRestaurant.getName());
        restaurant.setLocation(updatedRestaurant.getLocation());
        restaurant.setCuisine(updatedRestaurant.getCuisine());
        restaurant.setDescription(updatedRestaurant.getDescription());
        restaurant.setMaxCapacity(updatedRestaurant.getMaxCapacity());
        restaurant.setCurrentCapacity(updatedRestaurant.getCurrentCapacity());
        restaurant.setcurrentCrowdLevel(updatedRestaurant.getcurrentCrowdLevel());
        return restaurants.save(restaurant);
    }

    @Override
    @Transactional
    public void removeById(long id) {
        if(restaurants.existsById(id)) {
            restaurants.deleteById(id);
            return;
        }
        throw new RestaurantNotFoundException(id);
    }

    public Restaurant getRestaurantByUsername(String username) {
        Employee employee = employees.getEmployeeByUsername(username);

        Restaurant restaurant = employee.getRestaurant();
        if (restaurant == null) {
            throw new RestaurantNotFoundException(username);
        }

        return restaurant;
    }

    public User getRestaurantOwner(long id){
        Restaurant restaurant = getRestaurantById(id);
        List<Employee> employeeList = restaurant.getEmployees();
        User owner = null;
        for(Employee employee: employeeList){
            if(employee.getUser().getAuthorities().toString().equals("[ROLE_BUSINESS]")){
                owner = employee.getUser();
            }
        }
        if(owner == null){
            throw new RestaurantOwnerNotFoundException(id);
        }
        return owner;
    }
}
