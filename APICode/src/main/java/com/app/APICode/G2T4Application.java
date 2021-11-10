package com.app.APICode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

import com.app.APICode.crowdlevel.CrowdLevel;
import com.app.APICode.crowdlevel.CrowdLevelRepository;
import com.app.APICode.ctest.Ctest;
import com.app.APICode.ctest.CtestServiceImpl;
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
		//Admin 
		User admin = new User("admin@test.com", "admin", "admin1", null, encoder.encode("goodpassword"), true,
				"ROLE_ADMIN");
		admin.setEnabled(true);
		System.out.println("[Add user]: " + users.save(admin).getUsername());
		//User
		User user = new User("user@test.com", "user1", "User", "one", encoder.encode("testing123"), false, "ROLE_USER");
		user.setEnabled(true);
		users.save(user);

		//Restaurant
		RestaurantRepository restaurants = ctx.getBean(RestaurantRepository.class);
		Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
		testRestaurant.setCurrentCapacity(0);
		testRestaurant.setImageUrl("subway.jpeg");
		// testRestaurant.setCrowdLevel();
		testRestaurant = restaurants.save(testRestaurant);
		System.out.println("[Add restaurant]:" + testRestaurant.getName());
		
		//Business owner 
		User business_owner = new User("user2@test.com", "BusinessOne", "Business", "One", encoder.encode("testing12345"), false,"ROLE_BUSINESS");
		business_owner.setEnabled(true);
		Employee owner = new Employee(business_owner, "Owner");
		owner.setRestaurant(testRestaurant);	
		owner.setStatus("Approved");
		business_owner.setEmployee(owner);
		users.save(business_owner);

		// Employee
		User employee1 = new User("employee5@test.com", "employee1", "employee", "1", encoder.encode("testing12345"), false,"ROLE_EMPLOYEE");
		employee1.setEnabled(true);
		Employee employee = new Employee(employee1, "Employee");
		employee.setRestaurant(testRestaurant);
		employee.setStatus("Pending");
		employee1.setEmployee(employee);
		users.save(employee1);

		User employee2 = new User("business1@test.com", "business1", "business", "1", encoder.encode("testing12345"), false,"ROLE_BUSINESS");
		employee2.setEnabled(true);
		users.save(employee2);

		Employee employee22 = new Employee(employee2, "Owner");
		employee22.setRestaurant(testRestaurant);
		employee2.setEmployee(employee22);
		users.save(employee2);


		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		Ctest ctest1 = new Ctest("ART", date, "Positive");
		ctest1.setEmployee(employee1.getEmployee());

		//CrowdLevel
		CrowdLevelRepository crowdLevel = ctx.getBean(CrowdLevelRepository.class);
		CrowdLevel testCrowdLevel = new CrowdLevel(new Date(), "Low", 0, testRestaurant);
		crowdLevel.save(testCrowdLevel);

		System.out.println(testRestaurant.getName() + " " + "id: " + testRestaurant.getId());

		//Measures
		MeasureRepository measures = ctx.getBean(MeasureRepository.class);
		Measure testMeasureRestaurant = new Measure("Restaurant", 2, true, false);
		measures.save(testMeasureRestaurant);
		Measure testMeasureGym = new Measure("Gym", 50, true, false);
		measures.save(testMeasureGym);
		Measure testMeasureEvents = new Measure("Events", 1000, true, true);
		measures.save(testMeasureEvents);
		Measure testMeasureGathering = new Measure("Gathering", 2, true, true);
		measures.save(testMeasureGathering);
	}

}
