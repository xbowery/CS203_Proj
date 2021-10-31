package com.app.APICode.User;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;

import com.app.APICode.templates.LoginDetails;
import com.app.APICode.templates.TokenDetails;
import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;
import com.app.APICode.verificationtoken.VerificationToken;
import com.app.APICode.verificationtoken.VerificationTokenRepository;

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
public class UserIntegrationTest {
    
    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private UserRepository users;

    @Autowired
	private BCryptPasswordEncoder encoder;

    @Autowired
    private VerificationTokenRepository vTokens;

    @Autowired
    private TestRestTemplate restTemplate;

    private String tokenGeneratedAdmin;

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

        User normalUser = new User("test1@test.com", "test1", "test1", null, encoder.encode("password123"), true, "ROLE_USER");
        normalUser.setEnabled(true);
        users.save(normalUser);
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

    @AfterAll
	void tearDown(){
		// clear the database after each test
		users.deleteAll();
	}

    @Test
    public void getAllUsers_AdminUser_Success() throws Exception {
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        Response userListResponse = request.get(uriUsers);

        assertEquals(200, userListResponse.getStatusCode());
    }

    @Test
    public void getAllUsers_NormalUser_Failure() throws Exception {
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response userListResponse = request.get(uriUsers);

        assertEquals(403, userListResponse.getStatusCode());
    }

    @Test
    public void addNewUsers_AdminUser_Success() throws Exception {
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

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
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

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
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        User testUser = new User("testing@test.com", "test2", "test2", null, encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        String savedUsername = users.save(testUser).getUsername();

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        String updateUserDetails = "{\r\n" +
        "  \"email\": \"newusers@test.com\",\r\n" +
        "  \"username\": \"test2\",\r\n" +
        "  \"firstName\": \"New\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"testing123\"\r\n" +
        "}";

        Response updateResponse = request.body(updateUserDetails).put(uriUsers + "/" + savedUsername);

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        Response updateUserResponse = request.get(uriUsers + "/" + savedUsername);

        assertEquals(200, updateResponse.getStatusCode());
        assertEquals(200, updateUserResponse.getStatusCode());
        assertEquals("New", JsonPath.from(updateUserResponse.getBody().asString()).get("firstName"));
        assertEquals("User", JsonPath.from(updateUserResponse.getBody().asString()).get("lastName"));
    }

    @Test
    public void updateUsers_NormalUser_Failure() throws Exception {
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        User testUser = new User("testuser@test.com", "testuser", "testuser", null, encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        User savedUser = users.save(testUser);
        String savedUsername = savedUser.getUsername();

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        String updateUserDetails = "{\r\n" +
        "  \"email\": \"newuser@test.com\",\r\n" +
        "  \"username\": \"newuser\",\r\n" +
        "  \"firstName\": \"New\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"testing123\"\r\n" +
        "}";

        Response updateUserResponse = request.body(updateUserDetails).put(uriUsers + "/" + savedUsername);

        User updatedUser = users.findByUsername(savedUsername).orElse(null);

        assertEquals(200, updateUserResponse.getStatusCode());
        assertEquals("", updateUserResponse.getBody().asString());
        assertNotNull(updatedUser);
    }

    @Test
    public void deleteUsers_AdminUser_Success() throws Exception {
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        User testUser = new User("testing@test.com", "test2", "test2", null, encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        String savedUsername = users.save(testUser).getUsername();

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        Response deleteUserResponse = request.delete(uriUsers + "/" + savedUsername);

        User savedUser = users.findByUsername(savedUsername).orElse(null);

        assertEquals(200, deleteUserResponse.getStatusCode());
        assertNull(savedUser);
    }

    @Test
    public void deleteUsers_NormalUser_Failure() throws Exception {
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        User testUser = new User("testusers@test.com", "testuser3", "testuser3", null, encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        String savedUsername = users.save(testUser).getUsername();

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response deleteUserResponse = request.delete(uriUsers + "/" + savedUsername);

        User savedUser = users.findByUsername(savedUsername).orElse(null);

        assertEquals(200, deleteUserResponse.getStatusCode());
        assertNotNull(savedUser);
    }

    @Test
    public void registerNewUser_Success() throws Exception {
        URI uriRegister = new URI(baseUrl + port + "/api/v1/register");

        String newUserDetails = "{\r\n" +
        "  \"email\": \"testuser4@test.com\",\r\n" +
        "  \"username\": \"testuser4\",\r\n" +
        "  \"firstName\": \"Test\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"password123\"\r\n" +
        "}";

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");

        Response registerUserResponse = request.body(newUserDetails).post(uriRegister);

        assertEquals(201, registerUserResponse.getStatusCode());
        assertEquals("Test", JsonPath.from(registerUserResponse.getBody().asString()).get("firstName"));
        assertEquals("User", JsonPath.from(registerUserResponse.getBody().asString()).get("lastName"));
    }

    @Test
    public void registerSameUsername_Failure() throws Exception {
        URI uriRegister = new URI(baseUrl + port + "/api/v1/register");

        String newUserDetails = "{\r\n" +
        "  \"email\": \"testuser5@test.com\",\r\n" +
        "  \"username\": \"testuser5\",\r\n" +
        "  \"firstName\": \"Test\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"password123\"\r\n" +
        "}";

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");

        Response registerUser1Response = request.body(newUserDetails).post(uriRegister);

        request.header("Content-Type", "application/json");

        Response registerUser2Response = request.body(newUserDetails).post(uriRegister);

        assertEquals(201, registerUser1Response.getStatusCode());
        assertEquals(409, registerUser2Response.getStatusCode());
        assertEquals("Test", JsonPath.from(registerUser1Response.getBody().asString()).get("firstName"));
        assertEquals("User", JsonPath.from(registerUser1Response.getBody().asString()).get("lastName"));
    }

    @Test
    public void invalidVerificationToken() throws Exception {
        URI uriRegistrationConfirmation = new URI(baseUrl + port + "/api/v1/registrationConfirm");
       
        RequestSpecification request = RestAssured.given();

        Response invalidTokenResponse = request.get(uriRegistrationConfirmation + "?token=1234");

        assertEquals(200, invalidTokenResponse.getStatusCode());
        assertEquals("invalidToken", invalidTokenResponse.getBody().asString());
    }

    @Test
    public void validVerificationToken() throws Exception {
        URI uriRegistrationConfirmation = new URI(baseUrl + port + "/api/v1/registrationConfirm");
        URI uriRegister = new URI(baseUrl + port + "/api/v1/register");

        String newUserDetails = "{\r\n" +
        "  \"email\": \"testuser6@test.com\",\r\n" +
        "  \"username\": \"testuser6\",\r\n" +
        "  \"firstName\": \"Test6\",\r\n" +
        "  \"lastName\": \"User\",\r\n" +
        "  \"password\": \"password123\"\r\n" +
        "}";

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");

        Response registerUserResponse = request.body(newUserDetails).post(uriRegister);

        User user = users.findByUsername("testuser6").orElse(null);

        String vToken = (vTokens.findByUser(user).orElse(null)).getToken();

        Response validTokenResponse = request.get(uriRegistrationConfirmation + "?token=" + vToken);
        
        assertEquals(201, registerUserResponse.getStatusCode());
        assertNotNull(user);
        assertNotNull(vToken);
        assertEquals(200, validTokenResponse.getStatusCode());
        assertEquals("valid", validTokenResponse.getBody().asString());
    }
}
