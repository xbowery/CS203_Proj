package com.app.APICode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

import com.app.APICode.crowdlevel.CrowdLevel;
import com.app.APICode.crowdlevel.CrowdLevelRepository;
import com.app.APICode.ctest.Ctest;
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

		RestaurantRepository restaurants = ctx.getBean(RestaurantRepository.class);
		Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
		testRestaurant.setCurrentCapacity(0);
		// testRestaurant.setCrowdLevel();
		testRestaurant = restaurants.save(testRestaurant);
		System.out.println("[Add restaurant]:" + testRestaurant.getName());

		User employee1 = new User("employee5@test.com", "employee1", "employee", "1", encoder.encode("testing12345"), false,"ROLE_EMPLOYEE");
		employee1.setEnabled(true);
		users.save(employee1);
		Employee employee11 = new Employee(employee1);
		employee11.setRestaurant(testRestaurant);
		employee1.setEmployee(employee11);
		users.save(employee1);

		User employee2 = new User("business1@test.com", "business1", "business", "1", encoder.encode("testing12345"), false,"ROLE_BUSINESS");
		employee2.setEnabled(true);
		users.save(employee2);
		Employee employee22 = new Employee(employee2);
		employee22.setRestaurant(testRestaurant);
		employee2.setEmployee(employee22);
		users.save(employee2);


		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		Ctest ctest1 = new Ctest("ART", date, "Positive");
		ctest1.setEmployee(employee1.getEmployee());

		MeasureRepository measure = ctx.getBean(MeasureRepository.class);
		Measure testMeasure = new Measure(new Date(), "gym", 50, true, false, null);
		System.out.println("[Add measure]:" + measure.save(testMeasure).getCreationDateTime());

		CrowdLevelRepository crowdLevel = ctx.getBean(CrowdLevelRepository.class);
		CrowdLevel testCrowdLevel = new CrowdLevel(new Date(), "medium", 22, testRestaurant);
		crowdLevel.save(testCrowdLevel);
	}

}
