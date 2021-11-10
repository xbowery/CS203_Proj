package com.app.APICode.restaurant;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;
import com.app.APICode.employee.Employee;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

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
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
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

    @Autowired
    private TestRestTemplate restTemplate;

    private String tokenGeneratedAdmin;

    private String tokenGeneratedBusinessOwner;

    private String tokenGeneratedUser;

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

        Restaurant testRestaurant = new Restaurant("Ya Kun", "SMU SOE", "Western", "Serving good bread", 5);
        testRestaurant.setCurrentCapacity(0);
		restaurants.save(testRestaurant);

        User businessOwner = new User("business@test.com", "businessOne", "business", "One", encoder.encode("password123"), true, "ROLE_BUSINESS");
        businessOwner.setEnabled(true);
        Employee owner = new Employee(businessOwner, "Worker");
        owner.setRestaurant(testRestaurant);
        businessOwner.setEmployee(owner);
        users.save(businessOwner);

        User normalUser = new User("test1@test.com", "test1", "test1", null, encoder.encode("password123"), true, "ROLE_USER");
        normalUser.setEnabled(true);
        Employee normalEmployee = new Employee(normalUser, "Worker");
        normalEmployee.setRestaurant(testRestaurant);
        normalUser.setEmployee(normalEmployee);
        normalUser.setAuthorities("ROLE_EMPLOYEE");
        users.save(normalUser);
    }

    @BeforeEach
    void getAdminRequestToken() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("admin", "goodpassword"), TokenDetails.class);

        tokenGeneratedAdmin = result.getBody().getAccessToken();
    }

    @BeforeEach
    void getBusinessOwnerRequestToken() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("businessOne", "password123"), TokenDetails.class);

        tokenGeneratedBusinessOwner = result.getBody().getAccessToken();
    }

    @BeforeEach
    void getUserRequestToken() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("test1", "password123"), TokenDetails.class);

        tokenGeneratedUser = result.getBody().getAccessToken();
    }
    
	@AfterAll
	void tearDown(){
		// clear the database after each test
		users.deleteAll();
        restaurants.deleteAll();
	}

    @Test
    public void getRestaurants_Success() throws Exception {
        Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
        restaurants.save(testRestaurant);
        URI uri = new URI(baseUrl + port + "/api/v1/restaurants");

        given().get(uri).
        then().
            statusCode(200).
            body("size()", equalTo(7));
    }

    @Test
	public void getRestaurant_ValidID_Success() throws Exception {
		Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
        Long id = restaurants.save(testRestaurant).getId();
		URI uri = new URI(baseUrl + port + "/api/v1/restaurants/" + id);

		given().get(uri).
		then().
			statusCode(200).
			body("name", equalTo("Subway"), "location", equalTo("SMU SCIS"), "cuisine", equalTo("Western"));
		
	}

    @Test
    public void getRestaurantValidEmployee_BusinessOwner_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/restaurants/user/test1");

        given().headers(
            "Authorization",
            "Bearer " + tokenGeneratedBusinessOwner,
            "Content-Type", "application/json"
        ).when().
        get(uri).
        then().
            statusCode(200).
            body("name", equalTo("Ya Kun"), "location", equalTo("SMU SOE"), "cuisine", equalTo("Western"));
    }

    @Test
    public void getRestaurantValidEmployee_NormalUser_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/restaurants/user/test1");

        given().headers(
            "Authorization",
            "Bearer " + tokenGeneratedUser,
            "Content-Type", "application/json"
        ).when().
        get(uri).
        then().
            statusCode(403);
    }

    @Test
    public void getRestaurant_InvalidEmployee_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/restaurants/user/admin");

        given().headers(
            "Authorization",
            "Bearer " + tokenGeneratedBusinessOwner,
            "Content-Type", "application/json"
        ).when().
        get(uri).
        then().
            statusCode(404);
    }

    @Test
    public void addRestaurant_AdminUser_Success() throws Exception {
        URI uriRestaurant = new URI(baseUrl + port + "/api/v1/restaurants");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        String addRestaurantDetails = "{\r\n" +
        "  \"name\": \"Subway\",\r\n" +
        "  \"location\": \"SMU\",\r\n" +
        "  \"cuisine\": \"Western\",\r\n" +
        "  \"description\": \"Fast food chain\",\r\n" +
        "  \"maxCapacity\": 50\r\n" +
        "}";

        Response addRestaurantResponse = request.body(addRestaurantDetails).post(uriRestaurant);

        assertEquals(201, addRestaurantResponse.getStatusCode());
        assertEquals("Subway", JsonPath.from(addRestaurantResponse.getBody().asString()).get("name"));
    }

    @Test
    public void addRestaurant_NormalUser_Failure() throws Exception {
        URI uriRestaurant = new URI(baseUrl + port + "/api/v1/restaurants");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer "+ tokenGeneratedUser).header("Content-Type", "application/json");

        String addRestaurantDetails = "{\r\n" +
        "  \"name\": \"Subway\",\r\n" +
        "  \"location\": \"SMU\",\r\n" +
        "  \"cuisine\": \"Western\",\r\n" +
        "  \"description\": \"Fast food chain\",\r\n" +
        "  \"maxCapacity\": 50\r\n" +
        "}";

        Response addRestaurantResponse = request.body(addRestaurantDetails).post(uriRestaurant);

        assertEquals(403, addRestaurantResponse.getStatusCode());
    }

    @Test
    public void updateRestaurant_AdminUser_Success() throws Exception {
        URI uriRestaurant = new URI(baseUrl + port + "/api/v1/restaurants");

        RequestSpecification request = RestAssured.given();

        Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
        Long id = restaurants.save(testRestaurant).getId();

        request.header("Authorization", "Bearer "+ tokenGeneratedAdmin).header("Content-Type", "application/json");

        String updateRestaurantDetails = "{\r\n" +
        "  \"name\": \"Koufu\",\r\n" +
        "  \"location\": \"SMU\",\r\n" +
        "  \"cuisine\": \"All\",\r\n" +
        "  \"description\": \"Eatery\",\r\n" +
        "  \"maxCapacity\": 100\r\n" +
        "}";

        Response updateRestaurantResponse = request.body(updateRestaurantDetails).put(uriRestaurant + "/" + id);

        assertEquals(200, updateRestaurantResponse.getStatusCode());
        assertEquals("Koufu", JsonPath.from(updateRestaurantResponse.getBody().asString()).get("name"));
    }

    @Test
    public void updateRestaurant_NormalUser_Failure() throws Exception {
        URI uriRestaurant = new URI(baseUrl + port + "/api/v1/restaurants");

        RequestSpecification request = RestAssured.given();

        Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
        Long id = restaurants.save(testRestaurant).getId();

        request.header("Authorization", "Bearer "+ tokenGeneratedUser).header("Content-Type", "application/json");

        String updateRestaurantDetails = "{\r\n" +
        "  \"name\": \"Koufu\",\r\n" +
        "  \"location\": \"SMU\",\r\n" +
        "  \"cuisine\": \"All\",\r\n" +
        "  \"description\": \"Eatery\",\r\n" +
        "  \"maxCapacity\": 100\r\n" +
        "}";

        Response updateRestaurantResponse = request.body(updateRestaurantDetails).put(uriRestaurant + "/" + id);

        assertEquals(403, updateRestaurantResponse.getStatusCode());
    }

    @Test
    public void deleteRestaurant_AdminUser_Success() throws Exception {
        URI uriRestaurant = new URI(baseUrl + port + "/api/v1/restaurants");

        RequestSpecification request = RestAssured.given();

        Restaurant testRestaurant = new Restaurant("Bricklanes", "SMU", "Western", "Bar", 40);
        Long id = restaurants.save(testRestaurant).getId();

        request.header("Authorization", "Bearer "+ tokenGeneratedAdmin).header("Content-Type", "application/json");

        Response deleteRestaurantResponse = request.delete(uriRestaurant + "/" + id);

        Restaurant savedRestaurant = restaurants.findById(id).orElse(null);

        assertEquals(200, deleteRestaurantResponse.getStatusCode());
        assertNull(savedRestaurant);
    }

    @Test
    public void deleteRestaurant_NormalUser_Failure() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        URI uriRestaurant = new URI(baseUrl + port + "/api/v1/restaurants");

        RequestSpecification request = RestAssured.given();

        Restaurant testRestaurant = new Restaurant("Koufu", "SMU", "All", "Restaurant Food Chain", 100);
        Long id = restaurants.save(testRestaurant).getId();

        request.header("Authorization", "Bearer "+ tokenGeneratedUser).header("Content-Type", "application/json");

        Response deleteRestaurantResponse = request.delete(uriRestaurant + "/" + id);

        Restaurant savedRestaurant = restaurants.findById(id).orElse(null);

        assertEquals(403, deleteRestaurantResponse.getStatusCode());
        assertNotNull(savedRestaurant);
    }
}
