package com.app.APICode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

import com.app.APICode.employee.Employee;
import com.app.APICode.measure.*;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantRepository;
import com.app.APICode.user.*;

@SpringBootApplication
public class G2T4Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(G2T4Application.class, args);

		UserRepository users = ctx.getBean(UserRepository.class);
		BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);

		User user = new User("admin@test.com", "admin", "admin1", null, encoder.encode("goodpassword"), true,
				"ROLE_ADMIN");
		user.setEnabled(true);
		System.out.println("[Add user]: " + users.save(user).getUsername());
		users.save(new User("user@test.com", "user1", "User", "one", encoder.encode("testing123"), false, "ROLE_USER"));
		users.save(new User("user2@test.com", "user2", "User", "2", encoder.encode("testing12345"), false,"ROLE_BUSINESS"));

		User employee1 = new User("employee5@test.com", "employee1", "employee", "1", encoder.encode("testing12345"), false,"ROLE_EMPLOYEE");
		employee1.setEnabled(true);
		users.save(employee1);

		RestaurantRepository restaurants = ctx.getBean(RestaurantRepository.class);
		Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
		testRestaurant.setCurrentCapacity(0);
		// testRestaurant.setCrowdLevel();
		System.out.println("[Add restaurant]:" + restaurants.save(testRestaurant).getName());

		MeasureRepository measure = ctx.getBean(MeasureRepository.class);
		Measure testMeasure = new Measure(new Date(), "gym", 50, true, false, null);
		System.out.println("[Add measure]:" + measure.save(testMeasure).getCreationDateTime());
	}

}
