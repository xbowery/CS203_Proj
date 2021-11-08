package com.app.APICode.ctest;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.config.RedirectConfig.redirectConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class CtestIntegrationTest {
    
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
    private CtestRepository ctests;

    @Autowired
    private TestRestTemplate restTemplate;

    private String tokenGeneratedAdmin;

    private String tokenGeneratedEmployee;

    private String tokenGeneratedUser;

    private String tokenGeneratedBusinessOwner;

    private Long testRestaurantID;

    private Long ctestID;

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

        User normalUser = new User("testinguser@test.com", "testuser", "test", "user", encoder.encode("testpassword"), true, "ROLE_USER");
        normalUser.setEnabled(true);
        users.save(normalUser);

        Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast Food Chain", 50);
        testRestaurant.setCurrentCapacity(0);
		testRestaurantID = restaurants.save(testRestaurant).getId();

        User normalEmployee = new User("test1@test.com", "test1", "test1", null, encoder.encode("password123"), true, "ROLE_USER");
        normalEmployee.setEnabled(true);
        Employee employee = new Employee(normalEmployee, "Employee");
        employee.setRestaurant(testRestaurant);
        normalEmployee.setEmployee(employee);
        normalEmployee.setAuthorities("ROLE_EMPLOYEE");
        users.save(normalEmployee);

        Ctest newCtest = new Ctest("ART", new Date(System.currentTimeMillis()), "Negative");
        newCtest.setEmployee(employee);
        ctestID = ctests.save(newCtest).getId();

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
    void getEmployeeRequestToken() throws URISyntaxException {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("test1", "password123"), TokenDetails.class);

        tokenGeneratedEmployee = result.getBody().getAccessToken();
    }

    @BeforeEach
    void getUserRequestToken() throws URISyntaxException {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("testuser", "testpassword"), TokenDetails.class);

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
        ctests.deleteAll();
		users.deleteAll();
        restaurants.deleteAll();
	}

    @Test
    void getAllCtest_Employee_Success() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee).header("Content-Type", "application/json");

        Response ctestListResponse = request.get(uriCtest);

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("ART");
        expectedResult.add("ART");

        assertEquals(200, ctestListResponse.getStatusCode());
        assertEquals(expectedResult, JsonPath.from(ctestListResponse.getBody().asString()).get("type"));
    }

    @Test
    void getAllCtest_NormalUser_Forbidden() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response ctestListResponse = request.get(uriCtest);

        assertEquals(403, ctestListResponse.getStatusCode());
    }

    @Test
    void postNewCtest_Employee_Success() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee).header("Content-Type", "application/json");

        String newCtestDetails = "{\r\n" +
        "  \"type\": \"ART\",\r\n" +
        "  \"date\": \"2021-11-09\",\r\n" +
        "  \"result\": \"Negative\"\r\n" +
        "}";

        Response ctestListResponse = request.body(newCtestDetails).post(uriCtest);

        assertEquals(201, ctestListResponse.getStatusCode());
        assertEquals("ART", JsonPath.from(ctestListResponse.getBody().asString()).get("type"));
    }

    @Test
    void postNewCtest_NormalUser_Forbidden() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        String newCtestDetails = "{\r\n" +
        "  \"type\": \"ART\",\r\n" +
        "  \"date\": \"2021-11-09\",\r\n" +
        "  \"result\": \"Negative\"\r\n" +
        "}";

        Response ctestListResponse = request.body(newCtestDetails).post(uriCtest);

        assertEquals(403, ctestListResponse.getStatusCode());
    }

    @Test
    void updateValidCtest_Employee_Success() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/" + ctestID);

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee).header("Content-Type", "application/json");

        String updatedCtestDetails = "{\r\n" +
        "  \"type\": \"ART\",\r\n" +
        "  \"date\": \"2021-11-09\",\r\n" +
        "  \"result\": \"Positive\"\r\n" +
        "}";

        Response ctestListResponse = request.body(updatedCtestDetails).put(uriCtest);

        assertEquals(200, ctestListResponse.getStatusCode());
        assertEquals("ART", JsonPath.from(ctestListResponse.getBody().asString()).get("type"));
        assertEquals("Positive", JsonPath.from(ctestListResponse.getBody().asString()).get("result"));
    }

    @Test
    void updateInvalidCtest_Employee_Failure() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/0");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee).header("Content-Type", "application/json");

        String updatedCtestDetails = "{\r\n" +
        "  \"type\": \"ART\",\r\n" +
        "  \"date\": \"2021-11-09\",\r\n" +
        "  \"result\": \"Positive\"\r\n" +
        "}";

        Response ctestListResponse = request.body(updatedCtestDetails).put(uriCtest);

        assertEquals(404, ctestListResponse.getStatusCode());
    }

    @Test
    void updateInvalidCtest_NormalUser_Foribdden() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/" + ctestID);

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        String updatedCtestDetails = "{\r\n" +
        "  \"type\": \"ART\",\r\n" +
        "  \"date\": \"2021-11-09\",\r\n" +
        "  \"result\": \"Positive\"\r\n" +
        "}";

        Response ctestListResponse = request.body(updatedCtestDetails).put(uriCtest);

        assertEquals(403, ctestListResponse.getStatusCode());
    }

    @Test
    void deleteValidCtest_Employee_Success() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/" + ctestID);

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee).header("Content-Type", "application/json");

        Response ctestListResponse = request.delete(uriCtest);

        assertEquals(204, ctestListResponse.getStatusCode());
    }

    @Test
    void deleteInvalidCtest_Employee_Failure() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/0");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee).header("Content-Type", "application/json");

        Response ctestListResponse = request.delete(uriCtest);

        assertEquals(404, ctestListResponse.getStatusCode());
    }

    @Test
    void deleteCtest_NormalUser_Forbidden() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/" + ctestID);

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response ctestListResponse = request.delete(uriCtest);

        assertEquals(403, ctestListResponse.getStatusCode());
    }
}
