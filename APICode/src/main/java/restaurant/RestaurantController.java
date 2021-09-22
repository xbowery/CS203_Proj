package restaurant;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestaurantController {
    private RestaurantRepository restaurants;

    public RestaurantController(RestaurantRepository restaurants){
        this.restaurants = restaurants;
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurants() {
        return restaurants.findAll();
    }
   
}


