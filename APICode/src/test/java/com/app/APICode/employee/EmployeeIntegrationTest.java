package com.app.APICode.employee;

import static io.restassured.RestAssured.given;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.app.APICode.ctest.Ctest;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantRepository;
import com.app.APICode.templates.LoginDetails;
import com.app.APICode.templates.TokenDetails;
import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
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

    private String tokenGeneratedBusinessOwner;

    private Long testRestaurantID;

    private Long testRestaurant2ID;

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

        Restaurant testRestaurant2 = new Restaurant("McDonald's","PlazaSingapura","Western", "Fast Food Chain", 50);
        testRestaurant2.setCurrentCapacity(0);
		Long id2 = restaurants.save(testRestaurant2).getId();
        testRestaurant2ID = id2;

        User user = new User("pendingemployee1@test.com", "user5", "User", "five", encoder.encode("testing23456"), false, "ROLE_USER");
        user.setEnabled(true);
        Employee employee = new Employee(user, "HR Manager");
        employee.setRestaurant(testRestaurant);
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");

        users.save(user);

        User businessOwner = new User("user2@test.com", "BusinessOne", "Business", "One", encoder.encode("testing12345"), false,"ROLE_BUSINESS");
        businessOwner.setEnabled(true);
        Employee owner = new Employee(businessOwner, "Business Owner");
        owner.setRestaurant(testRestaurant);
        businessOwner.setEmployee(owner);
        users.save(businessOwner);
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
        URI uri = new URI(baseUrl + port + "/api/v1/employees");

        given().headers(
            "Authorization",
            "Bearer " + tokenGeneratedBusinessOwner,
            "Content-Type", "application/json"
        ).when()
        .get(uri)
        .then()
        .statusCode(200)
        .body("size()", equalTo(3));
    }

    @Test
    public void addEmployee_NewUsername_ReturnSavedEmployee() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/users/employee");

        User user2 = new User("pendingemployee3@test.com", "user7", "User", "seven", encoder.encode("testing23456"), false, "ROLE_USER");
        user2.setEnabled(true);
        users.save(user2);

        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("user7", "testing23456"), TokenDetails.class);

        String tokenGeneratedNewEmployee = result.getBody().getAccessToken();

        RequestSpecification request = RestAssured.given();
        String requestMessage = "{\r\n" +
        "  \"restaurantId\": " + testRestaurantID + ",\r\n" +
        "  \"designation\": \"Finance Head\"\r\n" +
        "}";
        request.header("Authorization", "Bearer " + tokenGeneratedNewEmployee).header("Content-Type", "application/json");
        Response addEmployeeResponse = request.body(requestMessage).post(uri);
        assertEquals(201, addEmployeeResponse.getStatusCode());
        assertEquals("Finance Head", JsonPath.from(addEmployeeResponse.getBody().asString()).get("designation"));
    }

    @Test
    public void addEmployee_InvalidRestaurantID_ReturnNull() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/users/employee");

        User user2 = new User("pendingemployee4@test.com", "user8", "User", "eight", encoder.encode("testing23456"), false, "ROLE_USER");
        user2.setEnabled(true);
        users.save(user2);

        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("user8", "testing23456"), TokenDetails.class);

        String tokenGeneratedNewEmployee = result.getBody().getAccessToken();

        RequestSpecification request = RestAssured.given();
        String requestMessage = "{\r\n" +
        "  \"restaurantId\": " + 0 + ",\r\n" +
        "  \"designation\": \"Finance Head\"\r\n" +
        "}";
        request.header("Authorization", "Bearer " + tokenGeneratedNewEmployee).header("Content-Type", "application/json");
        Response addEmployeeResponse = request.body(requestMessage).post(uri);

        assertEquals(404, addEmployeeResponse.getStatusCode());
    }

    @Test
    void approveEmployee_validUsername_ReturnSuccess() throws Exception{
        URI uri = new URI(baseUrl + port + "/api/v1/users/employee");
        User user2 = new User("pendingemployee6@test.com", "user10", "User", "ten", encoder.encode("testing23456"), false, "ROLE_USER");
        Employee employee = new Employee(user2,"HR Manager");
        Restaurant testRestaurant = restaurants.findById(testRestaurantID).orElse(null);
        employee.setRestaurant(testRestaurant);
        employee.setStatus("Pending");
        user2.setEmployee(employee);

        user2.setEnabled(true);
        users.save(user2);

        RequestSpecification request = RestAssured.given();
        String requestMessage = "{\r\n" +
        "  \"username\": \"user10\"\r\n" +
        "}";
        request.header("Authorization", "Bearer " + tokenGeneratedBusinessOwner).header("Content-Type", "application/json");
        Response updateEmployeeResponse = request.body(requestMessage).put(uri);
        assertEquals(200, updateEmployeeResponse.getStatusCode());
        assertEquals("Approved", JsonPath.from(updateEmployeeResponse.getBody().asString()).get("status"));
    }

    @Test 
    void deleteEmployee_validUsername_ReturnNull() throws Exception {

        URI uri = new URI(baseUrl + port + "/api/v1/users/employee/user5");

        RequestSpecification request = RestAssured.given();
    
        request.header("Authorization", "Bearer " + tokenGeneratedBusinessOwner).header("Content-Type", "application/json");
        Response addEmployeeResponse = request.delete(uri);

        assertEquals(204, addEmployeeResponse.getStatusCode());
    }

    @Test
    void deleteEmployee_InvalidUsername_ReturnNull() throws Exception {
        User user2 = new User("pendingemployee5@test.com", "user9", "User", "nine", encoder.encode("testing23456"), false, "ROLE_USER");
        user2.setEnabled(true);
        users.save(user2);
        URI uri = new URI(baseUrl + port + "/api/v1/users/employee/" + user2.getUsername());

        RequestSpecification request = RestAssured.given();
    
        request.header("Authorization", "Bearer " + tokenGeneratedBusinessOwner).header("Content-Type", "application/json");
        Response addEmployeeResponse = request.delete(uri);

        assertEquals(404, addEmployeeResponse.getStatusCode());
    }

    @Test
    void getCtests_ValidBusinessOwner_ReturnSuccess() throws Exception {
        User user1 = new User("pendingemployee11@test.com", "user13", "User", "thirteen", encoder.encode("testing23456"), false, "ROLE_USER");
        Employee employee = new Employee(user1, "HR Manager");
        Restaurant testRestaurant = restaurants.findById(testRestaurantID).orElse(null);
        employee.setRestaurant(testRestaurant);

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Ctest ctest = new Ctest("PCR",date,"Negative");
        ctest.setEmployee(employee);
        List<Ctest> allctests = new ArrayList<Ctest>();
        allctests.add(ctest);
        
        employee.setCtests(allctests);
        user1.setEmployee(employee);
        user1.setAuthorities("ROLE_EMPLOYEE");
        users.save(user1);

        URI uri = new URI(baseUrl + port + "/api/v1/users/employee/BusinessOne/ctests");
        RequestSpecification request = RestAssured.given();
    
        request.header("Authorization", "Bearer " + tokenGeneratedBusinessOwner).header("Content-Type", "application/json");
        Response getEmployeeResponse = request.get(uri);

        assertEquals(200, getEmployeeResponse.getStatusCode());
    }




}
