package com.app.APICode.crowdlevel;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.app.APICode.employee.Employee;
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
public class CrowdLevelIntegrationTest {
    
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
    private CrowdLevelRepository crowdLevels;

    @Autowired
    private TestRestTemplate restTemplate;

    private String tokenGeneratedBusinessOwner1;

    private String tokenGeneratedBusinessOwner2;
    
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
        Restaurant testRestaurant = new Restaurant("Ya Kun", "SMU SOE", "Western", "Serving good bread", 5);
        testRestaurant.setCurrentCapacity(0);
		restaurants.save(testRestaurant);

        Restaurant testRestaurant2 = new Restaurant("Hong Seng", "SMU SOE", "Chinese", "Serving good curry rice", 5);
        testRestaurant2.setCurrentCapacity(0);
		restaurants.save(testRestaurant2);

        User businessOwner = new User("business@test.com", "businessOne", "business", "One", encoder.encode("password123"), true, "ROLE_BUSINESS");
        businessOwner.setEnabled(true);
        Employee owner = new Employee(businessOwner, "Worker");
        owner.setRestaurant(testRestaurant);
        businessOwner.setEmployee(owner);
        users.save(businessOwner);

        User businessOwner2 = new User("business2@test.com", "businessTwo", "business", "Two", encoder.encode("password123"), true, "ROLE_BUSINESS");
        businessOwner2.setEnabled(true);
        Employee owner2 = new Employee(businessOwner2, "Worker");
        owner2.setRestaurant(testRestaurant2);
        businessOwner2.setEmployee(owner2);
        users.save(businessOwner2);

        User normalUser = new User("test1@test.com", "test1", "test1", null, encoder.encode("password123"), true, "ROLE_USER");
        normalUser.setEnabled(true);
        users.save(normalUser);

        CrowdLevel newCrowd = new CrowdLevel(new Date(System.currentTimeMillis()), null, 2, testRestaurant);
        newCrowd.setLatestCrowd();
        crowdLevels.save(newCrowd);
    }

    @BeforeEach
    void getBusinessOwner1RequestToken() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("businessOne", "password123"), TokenDetails.class);

        tokenGeneratedBusinessOwner1 = result.getBody().getAccessToken();
    }

    @BeforeEach
    void getBusinessOwner2RequestToken() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("businessTwo", "password123"), TokenDetails.class);

        tokenGeneratedBusinessOwner2 = result.getBody().getAccessToken();
    }

    @BeforeEach
    void getUserRequestToken() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("test1", "password123"), TokenDetails.class);

        tokenGeneratedUser = result.getBody().getAccessToken();
    }

    @AfterAll
    void tearDown() {
        crowdLevels.deleteAll();
        users.deleteAll();
        restaurants.deleteAll();
    }

    @Test
    public void getPopulatedCrowdLevel_BusinessOwner_Success() throws URISyntaxException {
        URI uriCrowdLevel = new URI(baseUrl + port + "/api/v1/restaurants/crowdLevel");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer "+ tokenGeneratedBusinessOwner1).header("Content-Type", "application/json");

        Response getCrowdLevelResponse = request.get(uriCrowdLevel);

        List<Integer> noOfCustomers = new ArrayList<>();

        noOfCustomers.add(2);
        noOfCustomers.add(3);

        assertEquals(200, getCrowdLevelResponse.getStatusCode());
        assertEquals(noOfCustomers, JsonPath.from(getCrowdLevelResponse.getBody().asString()).get("noOfCustomers"));
    }

    @Test
    public void getUnpopulatedCrowdLevel_BusinessOwner_Failure() throws URISyntaxException {
        URI uriCrowdLevel = new URI(baseUrl + port + "/api/v1/restaurants/crowdLevel");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer "+ tokenGeneratedBusinessOwner2).header("Content-Type", "application/json");

        Response getCrowdLevelResponse = request.get(uriCrowdLevel);

        assertEquals(200, getCrowdLevelResponse.getStatusCode());
    }

    @Test
    public void getCrowdLevel_NormalUser_Failure() throws URISyntaxException {
        URI uriCrowdLevel = new URI(baseUrl + port + "/api/v1/restaurants/crowdLevel");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer "+ tokenGeneratedUser).header("Content-Type", "application/json");

        Response getCrowdLevelResponse = request.get(uriCrowdLevel);

        assertEquals(403, getCrowdLevelResponse.getStatusCode());
    }

    @Test
    public void addCrowdLevel_BusinessOwner_Success() throws URISyntaxException {
        URI uriCrowdLevel = new URI(baseUrl + port + "/api/v1/restaurants/crowdLevel");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer "+ tokenGeneratedBusinessOwner1).header("Content-Type", "application/json");

        String addCrowdLevelDetails = "{\r\n" +
        "  \"datetime\": \"2021-11-10T05:03:27.949+00:00\",\r\n" +
        "  \"latestCrowd\": \"Low\",\r\n" +
        "  \"noOfCustomers\": 3\r\n" +
        "}";

        Response postCrowdLevelResponse = request.body(addCrowdLevelDetails).post(uriCrowdLevel);

        assertEquals(201, postCrowdLevelResponse.getStatusCode());
    }

    @Test
    public void addCrowdLevel_NormalUser_Failure() throws URISyntaxException {
        URI uriCrowdLevel = new URI(baseUrl + port + "/api/v1/restaurants/crowdLevel");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer "+ tokenGeneratedUser).header("Content-Type", "application/json");

        String addCrowdLevelDetails = "{\r\n" +
        "  \"datetime\": \"2021-11-10T05:03:27.949+00:00\",\r\n" +
        "  \"latestCrowd\": \"Low\",\r\n" +
        "  \"noOfCustomers\": 3\r\n" +
        "}";

        Response postCrowdLevelResponse = request.body(addCrowdLevelDetails).post(uriCrowdLevel);

        assertEquals(403, postCrowdLevelResponse.getStatusCode());
    }
}
