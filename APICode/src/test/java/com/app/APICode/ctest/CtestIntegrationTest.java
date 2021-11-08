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

    private String tokenGeneratedEmployee1;

    private String tokenGeneratedEmployee2;

    private String tokenGeneratedEmployee3;

    private String tokenGeneratedUser;

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

        User normalEmployee = new User("test1@test.com", "test1", "test1", null, encoder.encode("password123"), true, "ROLE_USER");
        normalEmployee.setEnabled(true);
        Employee employee = new Employee(normalEmployee, "Employee");
        employee.setRestaurant(testRestaurant);
        normalEmployee.setEmployee(employee);
        normalEmployee.setAuthorities("ROLE_EMPLOYEE");
        users.save(normalEmployee);

        User normalEmployee2 = new User("test2@test.com", "employee2", "employee", "two", encoder.encode("password123"), true, "ROLE_USER");
        normalEmployee2.setEnabled(true);
        Employee employee2 = new Employee(normalEmployee2, "Employee");
        employee2.setRestaurant(testRestaurant);
        normalEmployee2.setEmployee(employee2);
        normalEmployee2.setAuthorities("ROLE_EMPLOYEE");
        users.save(normalEmployee2);

        User normalEmployee3 = new User("test3@test.com", "employee3", "employee", "three", encoder.encode("password123"), true, "ROLE_USER");
        normalEmployee3.setEnabled(true);
        Employee employee3 = new Employee(normalEmployee3, "Employee");
        employee3.setRestaurant(testRestaurant);
        normalEmployee3.setEmployee(employee3);
        normalEmployee3.setAuthorities("ROLE_EMPLOYEE");
        users.save(normalEmployee3);

        Ctest newCtest = new Ctest("ART", new Date(System.currentTimeMillis()), "Negative");
        newCtest.setEmployee(employee);
        ctestID = ctests.save(newCtest).getId();

        Ctest newCtest1 = new Ctest("ART", new Date(System.currentTimeMillis() - 86400000), "Negative");
        newCtest1.setEmployee(employee2);
        ctestID = ctests.save(newCtest).getId();

        Ctest newCtest2 = new Ctest("ART", new Date(System.currentTimeMillis()), "Negative");
        newCtest2.setEmployee(employee2);
        ctestID = ctests.save(newCtest).getId();

        User businessOwner = new User("user2@test.com", "BusinessOne", "Business", "One", encoder.encode("testing12345"), false,"ROLE_BUSINESS");
        businessOwner.setEnabled(true);
        Employee owner = new Employee(businessOwner, "Business Owner");
        owner.setRestaurant(testRestaurant);
        businessOwner.setEmployee(owner);
        users.save(businessOwner);
    }

    @BeforeEach
    void getEmployee1RequestToken() throws URISyntaxException {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("test1", "password123"), TokenDetails.class);

        tokenGeneratedEmployee1 = result.getBody().getAccessToken();
    }

    @BeforeEach
    void getEmployee2RequestToken() throws URISyntaxException {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("employee2", "password123"), TokenDetails.class);

        tokenGeneratedEmployee2 = result.getBody().getAccessToken();
    }

    @BeforeEach
    void getEmployee3RequestToken() throws URISyntaxException {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("employee3", "password123"), TokenDetails.class);

        tokenGeneratedEmployee3 = result.getBody().getAccessToken();
    }

    @BeforeEach
    void getUserRequestToken() throws URISyntaxException {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("testuser", "testpassword"), TokenDetails.class);

        tokenGeneratedUser = result.getBody().getAccessToken();
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

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee1).header("Content-Type", "application/json");

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

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee1).header("Content-Type", "application/json");

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

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee1).header("Content-Type", "application/json");

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

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee1).header("Content-Type", "application/json");

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

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee1).header("Content-Type", "application/json");

        Response ctestListResponse = request.delete(uriCtest);

        assertEquals(204, ctestListResponse.getStatusCode());
    }

    @Test
    void deleteInvalidCtest_Employee_Failure() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/0");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee1).header("Content-Type", "application/json");

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

    @Test
    void getNextCtestEmployee_Has1Ctest_Success() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/next");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee1).header("Content-Type", "application/json");

        Response ctestListResponse = request.get(uriCtest);

        String result = "\"2021-11-16\"";

        assertEquals(200, ctestListResponse.getStatusCode());
        assertEquals(result, ctestListResponse.getBody().asString());
    }

    @Test
    void getNextCtestEmployee_HasMultipleCtests_Success() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/next");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee2).header("Content-Type", "application/json");

        Response ctestListResponse = request.get(uriCtest);

        String result = "\"" + (new Date(System.currentTimeMillis())).toString() + "\"";

        assertEquals(200, ctestListResponse.getStatusCode());
        assertEquals(result, ctestListResponse.getBody().asString());
    }

    @Test
    void getNextCtestEmployee_HasNoCtests_Success() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/next");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedEmployee3).header("Content-Type", "application/json");

        Response ctestListResponse = request.get(uriCtest);

        String result = "\"" + (new Date(System.currentTimeMillis())).toString() + "\"";


        assertEquals(200, ctestListResponse.getStatusCode());
        assertEquals(result, ctestListResponse.getBody().asString());
    }

    @Test
    void getNextCtest_NormalUser_Failure() throws URISyntaxException {
        URI uriCtest = new URI(baseUrl + port + "/api/v1/users/employee/ctests/next");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response ctestListResponse = request.get(uriCtest);

        assertEquals(403, ctestListResponse.getStatusCode());
    }
}
