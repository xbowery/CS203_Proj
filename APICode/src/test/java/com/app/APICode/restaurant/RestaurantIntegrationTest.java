package com.app.APICode.restaurant;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Optional;

import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantIntegrationTest {
    
    @LocalServerPort
	private int port;

	private final String baseUrl = "http://localhost:";

	@Autowired
	private RestaurantRepository restaurants;

	@Autowired
	private UserRepository users;

	@Autowired
	private BCryptPasswordEncoder encoder;

    @BeforeAll
    public static void initClass() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config()
            .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
            .redirect(redirectConfig().followRedirects(false));
    }

	@AfterEach
	void tearDown(){
		// clear the database after each test
		restaurants.deleteAll();
		users.deleteAll();
	}

    @Test
    public void getRestaurants_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/restaurants");

        given().get(uri).
        then().
            statusCode(200).
            body("size()", equalTo(2));
    }

    @Test
	public void getRestaurant_ValidRestaurantNameAndLocation_Success() throws Exception {
		Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
        restaurants.save(testRestaurant);
		String name = "Subway";
        String location = "SMU%20SCIS";
		URI uri = new URI(baseUrl + port + "/api/v1/restaurants/" + name + "/" + location);

		given().get(uri).
		then().
			statusCode(200).
			body("name", equalTo(name), "location", equalTo("SMU SCIS"), "cuisine", equalTo("Western"));
		
	}

    @Test
    public void addRestaurant_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/restaurants");
        User admin = new User("admin@test.com", "admin", "admin1", null, encoder.encode("goodpassword"), true,
        "ROLE_ADMIN");
        admin.setEnabled(true);

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "Subway");
        requestParams.put("location", "SMU SCIS");
        requestParams.put("cuisine", "Western");
        requestParams.put("description", "Fast Food Chain");
        requestParams.put("maxCapacity", 50);

        given().auth().basic("admin", "goodpassword")
			.accept("*/*").contentType("application/json")
			.body(requestParams.toJSONString()).post(uri).
		then().
			statusCode(201).
			body("name", equalTo("Subway"));
    }
}
