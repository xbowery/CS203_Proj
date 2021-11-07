package com.app.APICode.employee;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;

import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantRepository;
import com.app.APICode.templates.LoginDetails;
import com.app.APICode.templates.TokenDetails;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class EmployeeIntegrationTest {
    
    @LocalServerPort
	private int port;

	private final String baseUrl = "http://localhost:";

	@Autowired
	private UserRepository users;

	@Autowired
	private BCryptPasswordEncoder encoder;

    @Autowired
    private RestaurantRepository restaurants;

    @Autowired
    private TestRestTemplate restTemplate;

    private String tokenGeneratedAdmin;

    private String tokenGeneratedUser;

    private String tokenGeneratedBusinessOwner;

    private Long testRestaurantID;

    @BeforeAll
    public static void initClass() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config()
            .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
            .redirect(redirectConfig().followRedirects(false));
    }

    @BeforeAll
    void setupDB() {
        User admin = new User("admin@test.com", "admin", "admin1", null, encoder.encode("goodpassword"), true, "ROLE_ADMIN");
        admin.setEnabled(true);
        users.save(admin);

        User normalUser = new User("test1@test.com", "test1", "test1", null, encoder.encode("password123"), true, "ROLE_USER");
        normalUser.setEnabled(true);
        users.save(normalUser);

        Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
        testRestaurant.setCurrentCapacity(0);
		Long id = restaurants.save(testRestaurant).getId();
        testRestaurantID = id;

        User businessOwner = new User("user2@test.com", "BusinessOne", "Business", "One", encoder.encode("testing12345"), false,"ROLE_BUSINESS");
        businessOwner.setEnabled(true);
        Employee owner = new Employee(businessOwner, "Business Owner");
        owner.setRestaurant(testRestaurant);
        businessOwner.setEmployee(owner);
        users.save(businessOwner);
    }

    @BeforeEach
    void getAdminRequestToken() throws URISyntaxException {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("admin", "goodpassword"), TokenDetails.class);

        tokenGeneratedAdmin = result.getBody().getAccessToken();
    }

    @BeforeEach
    void getUserRequestToken() throws URISyntaxException {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("test1", "password123"), TokenDetails.class);

        tokenGeneratedUser = result.getBody().getAccessToken();
    }

    @BeforeEach
    void getBusinessOwnerRequestToken() throws URISyntaxException {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("BusinessOne", "testing12345"), TokenDetails.class);

        tokenGeneratedBusinessOwner = result.getBody().getAccessToken();
    }
    
	@AfterAll
	void tearDown(){
		// clear the database after each test
		users.deleteAll();
        restaurants.deleteAll();
	}

    @Test
    public void getEmployees_Success() throws Exception{
        User user = new User("pendingemployee1@test.com", "user5", "User", "five", encoder.encode("testing23456"), false, "ROLE_USER");
        Employee employee = new Employee(user, "HR Manager");
        Restaurant testRestaurant = restaurants.findById(testRestaurantID).orElse(null);
        employee.setRestaurant(testRestaurant);
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");

        users.save(user);

        URI uri = new URI(baseUrl + port + "/api/v1/employees");

        given().headers(
            "Authorization",
            "Bearer " + tokenGeneratedBusinessOwner,
            "Content-Type", "application/json"
        ).when()
        .get(uri)
        .then()
        .statusCode(200)
        .body("size()", equalTo(2));
    }

    @Test
    public void getEmployee_ValidUsername_Success() throws Exception {
        User user1 = new User("pendingemployee2@test.com", "user6", "User", "six", encoder.encode("testing23456"), false, "ROLE_USER");
        Employee employee = new Employee(user1, "HR Manager");
        Restaurant testRestaurant = restaurants.findById(testRestaurantID).orElse(null);
        employee.setRestaurant(testRestaurant);
        user1.setEmployee(employee);
        user1.setAuthorities("ROLE_EMPLOYEE");

        users.save(user1);

        URI uri = new URI(baseUrl + port + "/api/v1/users/employee/" + user1.getUsername());

        given().headers(
            "Authorization",
            "Bearer " + tokenGeneratedBusinessOwner,
            "Content-Type", "application/json"
        ).when()
        .get(uri)
        .then()
        .statusCode(200)
        .body("designation", equalTo("HR Manager"));
    }

}
