package com.app.APICode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

import com.app.APICode.measure.hawker.MeasureHawker;
import com.app.APICode.measure.hawker.MeasureHawkerRepository;
import com.app.APICode.measure.others.MeasureOthers;
import com.app.APICode.measure.others.MeasureOthersRepository;
import com.app.APICode.measure.restaurant.MeasureRestaurant;
import com.app.APICode.measure.restaurant.MeasureRestaurantRepository;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantRepository;
import com.app.APICode.user.*;

@SpringBootApplication
public class G2T4Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(G2T4Application.class, args);

		UserRepository users = ctx.getBean(UserRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        System.out.println("[Add user]: " + users.save(
            new User("admin@test.com", "admin", encoder.encode("goodpassword"), true, "ROLE_ADMIN")).getUsername());
		users.save(new User("user@test.com", "user1", encoder.encode("testing123"), false, "ROLE_USER")).getUsername();
		
		RestaurantRepository restaurants = ctx.getBean(RestaurantRepository.class);
		Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
		System.out.println("[Add restaurant]:" + restaurants.save(testRestaurant).getName());
		testRestaurant.setCurrentCapacity(0);
		testRestaurant.setCrowdLevel();

		MeasureHawkerRepository measureHawkers = ctx.getBean(MeasureHawkerRepository.class);
		MeasureHawker testHawkerMeasure = new MeasureHawker(new Date(), 2, true);
		System.out.println("[Add hawker measure]:" + measureHawkers.save(testHawkerMeasure).getCreationDateTime());
	
		// MeasureOthersRepository measureOthers = ctx.getBean(MeasureOthersRepository.class);
		// MeasureOthers testOthersMeasure = new MeasureOthers(new Date(), "book store", 50, true, true, "2 per table");
		// System.out.println("[Add others measure]:" + measureOthers.save(testOthersMeasure).getCreationDateTime());

		// MeasureRestaurantRepository measureRestaurants = ctx.getBean(MeasureRestaurantRepository.class);
		// MeasureRestaurant testRestaurantMeasure = new MeasureRestaurant(new Date(), 2, true, true);
		// System.out.println("[Add restaurant measure]:" + measureRestaurants.save(testRestaurantMeasure).getCreationDateTime());

	}

}
