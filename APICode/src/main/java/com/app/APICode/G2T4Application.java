package com.app.APICode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.app.APICode.crowdlevel.CrowdLevel;
import com.app.APICode.crowdlevel.CrowdLevelRepository;
import com.app.APICode.ctest.Ctest;
import com.app.APICode.ctest.CtestRepository;
import com.app.APICode.ctest.CtestServiceImpl;
import com.app.APICode.employee.Employee;
import com.app.APICode.measure.*;
import com.app.APICode.notification.Notification;
import com.app.APICode.notification.NotificationRepository;
import com.app.APICode.notification.NotificationService;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantRepository;
import com.app.APICode.user.*;

@SpringBootApplication
public class G2T4Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(G2T4Application.class, args);

		UserRepository users = ctx.getBean(UserRepository.class);
		BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
		// Admin
		User admin = new User("admin@test.com", "admin", "admin1", null, encoder.encode("goodpassword"), true,
				"ROLE_ADMIN");
		admin.setEnabled(true);
		System.out.println("[Add user]: " + users.save(admin).getUsername());

		// User
		User user = new User("user@test.com", "user1", "User", "one", encoder.encode("testing123"), false, "ROLE_USER");
		user.setEnabled(true);
		users.save(user);

		// Restaurant
		RestaurantRepository restaurants = ctx.getBean(RestaurantRepository.class);
		Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
		testRestaurant.setCurrentCapacity(0);
		testRestaurant.setImageUrl("subway.jpeg");
		// testRestaurant.setCrowdLevel();
		testRestaurant = restaurants.save(testRestaurant);
		System.out.println("[Add restaurant]:" + testRestaurant.getName());

		//Crowd Level of restaurant
		CrowdLevel day1 = new CrowdLevel(new GregorianCalendar(2021, Calendar.NOVEMBER, 11).getTime(), "Medium", 30, testRestaurant);
		CrowdLevel day2 = new CrowdLevel(new GregorianCalendar(2021, Calendar.NOVEMBER, 10).getTime(), "High", 40, testRestaurant);
		CrowdLevel day3 = new CrowdLevel(new GregorianCalendar(2021, Calendar.NOVEMBER, 9).getTime(), "Medium", 30, testRestaurant);
		CrowdLevel day4 = new CrowdLevel(new GregorianCalendar(2021, Calendar.NOVEMBER, 8).getTime(), "Low", 10, testRestaurant);
		CrowdLevel day5 = new CrowdLevel(new GregorianCalendar(2021, Calendar.NOVEMBER, 7).getTime(), "Medium", 30, testRestaurant);
		CrowdLevel day6 = new CrowdLevel(new GregorianCalendar(2021, Calendar.NOVEMBER, 6).getTime(), "Low", 20, testRestaurant);
		List<CrowdLevel> chartData = new ArrayList<>();
		chartData.add(day1);
		chartData.add(day2);
		chartData.add(day3);
		chartData.add(day4);
		chartData.add(day5);
		chartData.add(day6);
		testRestaurant.setCrowdLevel(chartData);
		restaurants.save(testRestaurant);

		//Business owner 
		User businessOwner = new User("user2@test.com", "BusinessOne", "Business", "One", encoder.encode("testing12345"), false,"ROLE_BUSINESS");
		businessOwner.setEnabled(true);
		Employee owner = new Employee(businessOwner, "Owner");
		owner.setRestaurant(testRestaurant);	
		owner.setStatus("Approved");
		businessOwner.setEmployee(owner);
		users.save(businessOwner);

		// Employee
		User employee1 = new User("employee5@test.com", "employee1", "employee", "1", encoder.encode("testing12345"),
				false, "ROLE_EMPLOYEE");
		employee1.setEnabled(true);
		Employee employee = new Employee(employee1, "Employee");
		employee.setRestaurant(testRestaurant);
		employee.setStatus("Pending");
		employee1.setEmployee(employee);
		users.save(employee1);

		User employee2 = new User("business1@test.com", "business1", "business", "1", encoder.encode("testing12345"),
				false, "ROLE_BUSINESS");
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

		NotificationService notifications = ctx.getBean(NotificationService.class);
		notifications.addNewNotification("Welcome to Swisshack, admin!", admin);
		
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

		//Ctests
		CtestRepository ctests = ctx.getBean(CtestRepository.class);
		Ctest testCtest = new Ctest("ART", date, "Negative");
		testCtest.setEmployee(employee1.getEmployee());
		ctests.save(testCtest);
	}

}
