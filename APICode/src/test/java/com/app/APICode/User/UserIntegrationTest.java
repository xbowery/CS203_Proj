package com.app.APICode.User;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import com.app.APICode.templates.LoginDetails;
import com.app.APICode.templates.TokenDetails;
import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class UserIntegrationTest {
    
    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private UserRepository users;

    @Autowired
	private BCryptPasswordEncoder encoder;

    @Autowired
    private TestRestTemplate restTemplate;

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
    }

    @AfterAll
	void tearDown(){
		// clear the database after each test
		users.deleteAll();
	}

    @Test
    public void getAllUsers_AdminUser_Success() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        RequestSpecification request = RestAssured.given();

        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("admin", "goodpassword"), TokenDetails.class);

        String tokenGenerated = result.getBody().getAccessToken();

        request.header("Authorization", "Bearer " + tokenGenerated).header("Content-Type", "application/json");

        Response userListResponse = request.get(uriUsers);

        assertEquals(200, userListResponse.getStatusCode());
    }

    @Test
    public void getAllUsers_NormalUser_Failure() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        RequestSpecification request = RestAssured.given();

        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("test1", "password123"), TokenDetails.class);

        String tokenGenerated = result.getBody().getAccessToken();

        request.header("Authorization", "Bearer " + tokenGenerated).header("Content-Type", "application/json");

        Response userListResponse = request.get(uriUsers);

        assertEquals(403, userListResponse.getStatusCode());
    }

    @Test
    public void addNewUsers_AdminUser_Success() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        RequestSpecification request = RestAssured.given();

        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("admin", "goodpassword"), TokenDetails.class);

        String tokenGenerated = result.getBody().getAccessToken();

        request.header("Authorization", "Bearer " + tokenGenerated).header("Content-Type", "application/json");

        String addUserDetails = "{\r\n" +
        "  \"email\": \"newuser@test.com\",\r\n" +
        "  \"username\": \"newuser\",\r\n" +
        "  \"firstName\": \"New\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"testing123\"\r\n" +
        "}";

        Response addUserResponse = request.body(addUserDetails).post(uriUsers);

        assertEquals(201, addUserResponse.getStatusCode());
        assertEquals("newuser", JsonPath.from(addUserResponse.getBody().asString()).get("username"));
    }

    @Test
    public void addNewUsers_NormalUser_Failure() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        RequestSpecification request = RestAssured.given();

        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("test1", "password123"), TokenDetails.class);

        String tokenGenerated = result.getBody().getAccessToken();

        request.header("Authorization", "Bearer " + tokenGenerated).header("Content-Type", "application/json");

        String addUserDetails = "{\r\n" +
        "  \"email\": \"newuser@test.com\",\r\n" +
        "  \"username\": \"newuser\",\r\n" +
        "  \"firstName\": \"New\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"testing123\"\r\n" +
        "}";

        Response addUserResponse = request.body(addUserDetails).post(uriUsers);

        assertEquals(403, addUserResponse.getStatusCode());
    }

    @Test
    public void updateUsers_AdminUser_Success() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        User testUser = new User("testing@test.com", "test2", "test2", null, encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        User savedUser = users.save(testUser);
        String savedUsername = savedUser.getUsername();

        System.out.println(savedUsername);

        RequestSpecification request = RestAssured.given();

        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("admin", "goodpassword"), TokenDetails.class);

        String tokenGenerated = result.getBody().getAccessToken();

        request.header("Authorization", "Bearer " + tokenGenerated).header("Content-Type", "application/json");

        String updateUserDetails = "{\r\n" +
        "  \"email\": \"newuser@test.com\",\r\n" +
        "  \"username\": \"newuser\",\r\n" +
        "  \"firstName\": \"New\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"testing123\"\r\n" +
        "}";

        Response updateUserResponse = request.body(updateUserDetails).put(uriUsers + "/" + savedUsername);

        System.out.println(updateUserResponse.getBody().asString());

        assertEquals(200, updateUserResponse.getStatusCode());
        assertEquals("New", JsonPath.from(updateUserResponse.getBody().asString()).get("firstName"));
        assertEquals("User", JsonPath.from(updateUserResponse.getBody().asString()).get("lastName"));
    }

    @Test
    public void updateUsers_NormalUser_Failure() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        User testUser = new User("testuser@test.com", "testuser", "testuser", null, encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        User savedUser = users.save(testUser);
        String savedUsername = savedUser.getUsername();

        RequestSpecification request = RestAssured.given();

        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("test1", "password123"), TokenDetails.class);

        String tokenGenerated = result.getBody().getAccessToken();

        request.header("Authorization", "Bearer " + tokenGenerated).header("Content-Type", "application/json");

        String addUserDetails = "{\r\n" +
        "  \"email\": \"newuser@test.com\",\r\n" +
        "  \"username\": \"newuser\",\r\n" +
        "  \"firstName\": \"New\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"testing123\"\r\n" +
        "}";

        Response addUserResponse = request.body(addUserDetails).put(uriUsers + "/" + savedUsername);

        assertEquals(403, addUserResponse.getStatusCode());
    }

    @Test
    public void deleteUsers_AdminUser_Success() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        User testUser = new User("testing@test.com", "test2", "test2", null, encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        User savedUser = users.save(testUser);
        String savedUsername = savedUser.getUsername();

        RequestSpecification request = RestAssured.given();

        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("admin", "goodpassword"), TokenDetails.class);

        String tokenGenerated = result.getBody().getAccessToken();

        request.header("Authorization", "Bearer " + tokenGenerated).header("Content-Type", "application/json");

        String updateUserDetails = "{\r\n" +
        "  \"email\": \"newuser@test.com\",\r\n" +
        "  \"username\": \"newuser\",\r\n" +
        "  \"firstName\": \"New\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"testing123\"\r\n" +
        "}";

        Response addUserResponse = request.body(updateUserDetails).put(uriUsers + "/" + savedUsername);

        assertEquals(200, addUserResponse.getStatusCode());
        assertEquals("newuser", JsonPath.from(addUserResponse.getBody().asString()).get("username"));
    }

    @Test
    public void deleteUsers_NormalUser_Failure() throws Exception {
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        User testUser = new User("testuser@test.com", "testuser", "testuser", null, encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        User savedUser = users.save(testUser);
        String savedUsername = savedUser.getUsername();

        RequestSpecification request = RestAssured.given();

        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("test1", "password123"), TokenDetails.class);

        String tokenGenerated = result.getBody().getAccessToken();

        request.header("Authorization", "Bearer " + tokenGenerated).header("Content-Type", "application/json");

        String addUserDetails = "{\r\n" +
        "  \"email\": \"newuser@test.com\",\r\n" +
        "  \"username\": \"newuser\",\r\n" +
        "  \"firstName\": \"New\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"testing123\"\r\n" +
        "}";

        Response addUserResponse = request.body(addUserDetails).put(uriUsers + "/" + savedUsername);

        assertEquals(403, addUserResponse.getStatusCode());
    }
}
