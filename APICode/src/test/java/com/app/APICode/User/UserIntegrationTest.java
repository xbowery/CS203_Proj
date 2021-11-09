package com.app.APICode.User;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
import static org.hamcrest.Matchers.equalTo;

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

        User testUser = new User("testinguser@test.com", "testuser1", "test", "user", encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        users.save(testUser);
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
        request.then().body("size()", equalTo(12));
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
    public void getSpecificExistingUser_AdminUser_Success() throws Exception {
        URI uriUser = new URI(baseUrl + port + "/api/v1/users/testuser1");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        Response userListResponse = request.get(uriUser);

        assertEquals(200, userListResponse.getStatusCode());
        assertEquals("testuser1", JsonPath.from(userListResponse.getBody().asString()).get("username"));
    }

    @Test
    public void getSpecificNonExistentUser_AdminUser_Failure() throws Exception {
        URI uriUser = new URI(baseUrl + port + "/api/v1/users/nosuchuser");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        Response userListResponse = request.get(uriUser);

        assertEquals(404, userListResponse.getStatusCode());
    }

    @Test
    public void getOwnself_NormalUser_Success() throws Exception {
        URI uriUser = new URI(baseUrl + port + "/api/v1/users/test1");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response userListResponse = request.get(uriUser);

        assertEquals(200, userListResponse.getStatusCode());
        assertEquals("test1", JsonPath.from(userListResponse.getBody().asString()).get("username"));
    }

    @Test
    public void getOtherUser_NormalUser_Failure() throws Exception {
        URI uriUser = new URI(baseUrl + port + "/api/v1/users/testuser1");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response userListResponse = request.get(uriUser);

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

        Response updateResponse = request.body(updateUserDetails).put(uriUsers);

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

        Response updateUserResponse = request.body(updateUserDetails).put(uriUsers);

        User updatedUser = users.findByUsername(savedUsername).orElse(null);

        assertEquals(403, updateUserResponse.getStatusCode());
        assertNotNull(updatedUser);
    }

    @Test
    public void updateOwnselfPassword_NormalUser_Success() throws Exception {
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        User testUser = new User("newlytested@test.com", "newlyuser", "newly", "user", encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        User savedUser = users.save(testUser);
        String savedUsername = savedUser.getUsername();

        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("newlyuser", "password123"), TokenDetails.class);

        String tokenGeneratedDummyUser = result.getBody().getAccessToken();

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedDummyUser).header("Content-Type", "application/json");

        String updateUserDetails = "{\r\n" +
        "  \"email\": \"newlytested@test.com\",\r\n" +
        "  \"username\": \"newlyuser\",\r\n" +
        "  \"firstName\": \"newly\",\r\n" +
        "  \"lastName\": \"user\",\r\n" +
        "  \"password\": \"testing12345\"\r\n" +
        "}";

        Response updateResponse = request.body(updateUserDetails).put(uriUsers);

        User updatedUser = users.findByUsername(savedUsername).orElse(null);

        request.header("Authorization", "Bearer " + tokenGeneratedDummyUser).header("Content-Type", "application/json");

        Response updateUserResponse = request.get(uriUsers + "/" + savedUsername);

        assertEquals(200, updateResponse.getStatusCode());
        assertEquals(200, updateUserResponse.getStatusCode());
        assertNotNull(updatedUser);
        assertEquals("newly", JsonPath.from(updateUserResponse.getBody().asString()).get("firstName"));
        assertEquals("user", JsonPath.from(updateUserResponse.getBody().asString()).get("lastName"));
    }

    @Test
    public void updateExistingEmail_NormalUser_Failure() throws Exception {
        URI uriUsers = new URI(baseUrl + port + "/api/v1/users");

        User testUser = new User("newlytested1@test.com", "newlyuser1", "newly", "user1", encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        User savedUser = users.save(testUser);
        String savedUsername = savedUser.getUsername();

        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("newlyuser1", "password123"), TokenDetails.class);

        String tokenGeneratedDummyUser = result.getBody().getAccessToken();

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedDummyUser).header("Content-Type", "application/json");

        String updateUserDetails = "{\r\n" +
        "  \"email\": \"admin@test.com\",\r\n" +
        "  \"username\": \"newlyuser1\",\r\n" +
        "  \"firstName\": \"newly\",\r\n" +
        "  \"lastName\": \"user\",\r\n" +
        "  \"password\": \"testing12345\"\r\n" +
        "}";

        Response updateResponse = request.body(updateUserDetails).put(uriUsers);

        User updatedUser = users.findByUsername(savedUsername).orElse(null);

        assertEquals(409, updateResponse.getStatusCode());
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

        assertEquals(403, deleteUserResponse.getStatusCode());
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

        assertEquals(204, registerUserResponse.getStatusCode());
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

        assertEquals(204, registerUser1Response.getStatusCode());
        assertEquals(409, registerUser2Response.getStatusCode());
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
        
        assertEquals(204, registerUserResponse.getStatusCode());
        assertNotNull(user);
        assertNotNull(vToken);
        assertEquals(200, validTokenResponse.getStatusCode());
        assertEquals("valid", validTokenResponse.getBody().asString());
    }

    @Test
    public void resetPassword_ValidEmail_Success() throws Exception {
        URI uriResetPassword = new URI(baseUrl + port + "/api/v1/forgotPassword");

        Map<String, String> payload = new HashMap<>();
        payload.put("email", "testinguser@test.com");

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");

        Response forgotPasswordResponse = request.body(payload).post(uriResetPassword);

        assertEquals(204, forgotPasswordResponse.getStatusCode());
    }

    @Test
    public void resetPassword_InvalidEmail_Success() throws Exception {
        URI uriResetPassword = new URI(baseUrl + port + "/api/v1/forgotPassword");

        Map<String, String> payload = new HashMap<>();
        payload.put("email", "nosuchuser@test.com");

        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");

        Response forgotPasswordResponse = request.body(payload).post(uriResetPassword);

        assertEquals(204, forgotPasswordResponse.getStatusCode());
    }

    @Test
    public void changePasswordNewPassword_MatchingConfirmation_Success() throws Exception {
        URI uriChangePassword = new URI(baseUrl + port + "/api/v1/users/password");
        
        User testUser = new User("testuser7@test.com", "testuser7", "test", "user7", encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        users.save(testUser);
        
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("testuser7", "password123"), TokenDetails.class);

        String tokenGeneratedDummyUser = result.getBody().getAccessToken();

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedDummyUser).header("Content-Type", "application/json");

        String updateUserPasswordDetails = "{\r\n" +
        "  \"currentPassword\": \"password123\",\r\n" +
        "  \"newPassword\": \"password1234\",\r\n" +
        "  \"cfmPassword\": \"password1234\"\r\n" +
        "}";

        Response changePasswordResponse = request.body(updateUserPasswordDetails).post(uriChangePassword);

        assertEquals(204, changePasswordResponse.getStatusCode());
    }

    @Test
    public void changePasswordNewPassword_NotMatchingConfirmation_Failure() throws Exception {
        URI uriChangePassword = new URI(baseUrl + port + "/api/v1/users/password");
        
        User testUser = new User("testuser8@test.com", "testuser8", "test", "user8", encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        users.save(testUser);
        
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("testuser8", "password123"), TokenDetails.class);

        String tokenGeneratedDummyUser = result.getBody().getAccessToken();

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedDummyUser).header("Content-Type", "application/json");

        String updateUserPasswordDetails = "{\r\n" +
        "  \"currentPassword\": \"password123\",\r\n" +
        "  \"newPassword\": \"password1234\",\r\n" +
        "  \"cfmPassword\": \"password12345\"\r\n" +
        "}";

        Response changePasswordResponse = request.body(updateUserPasswordDetails).post(uriChangePassword);

        assertEquals(400, changePasswordResponse.getStatusCode());
    }

    @Test
    public void changePassword_WrongExistingPassword_Failure() throws Exception {
        URI uriChangePassword = new URI(baseUrl + port + "/api/v1/users/password");
        
        User testUser = new User("testuser9@test.com", "testuser9", "test", "user9", encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        users.save(testUser);
        
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("testuser9", "password123"), TokenDetails.class);

        String tokenGeneratedDummyUser = result.getBody().getAccessToken();

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedDummyUser).header("Content-Type", "application/json");

        String updateUserPasswordDetails = "{\r\n" +
        "  \"currentPassword\": \"password1234\",\r\n" +
        "  \"newPassword\": \"password12345\",\r\n" +
        "  \"cfmPassword\": \"password12345\"\r\n" +
        "}";

        Response changePasswordResponse = request.body(updateUserPasswordDetails).post(uriChangePassword);

        assertEquals(400, changePasswordResponse.getStatusCode());
    }

    @Test
    public void changePassword_MatchingExistingPassword_Failure() throws Exception {
        URI uriChangePassword = new URI(baseUrl + port + "/api/v1/users/password");
        
        User testUser = new User("testuser10@test.com", "testuser10", "test", "user10", encoder.encode("password123"), true, "ROLE_USER");
        testUser.setEnabled(true);
        users.save(testUser);
        
        URI uriLogin = new URI(baseUrl + port + "/api/v1/login");
        
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uriLogin,
                new LoginDetails("testuser10", "password123"), TokenDetails.class);

        String tokenGeneratedDummyUser = result.getBody().getAccessToken();

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedDummyUser).header("Content-Type", "application/json");

        String updateUserPasswordDetails = "{\r\n" +
        "  \"currentPassword\": \"password123\",\r\n" +
        "  \"newPassword\": \"password123\",\r\n" +
        "  \"cfmPassword\": \"password123\"\r\n" +
        "}";

        Response changePasswordResponse = request.body(updateUserPasswordDetails).post(uriChangePassword);

        assertEquals(400, changePasswordResponse.getStatusCode());
    }
}
