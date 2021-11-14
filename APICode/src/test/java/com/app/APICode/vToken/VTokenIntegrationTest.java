package com.app.APICode.vToken;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;
import com.app.APICode.verificationtoken.VerificationTokenRepository;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class VTokenIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
            .withPerMethodLifecycle(false);

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private UserRepository users;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private VerificationTokenRepository vTokens;

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
        User admin = new User("admin@test.com", "admin", "admin1", null, encoder.encode("goodpassword"), true,
                "ROLE_ADMIN");
        admin.setEnabled(true);
        users.save(admin);

        User normalUser = new User("test1@test.com", "test1", "test1", null, encoder.encode("password123"), true,
                "ROLE_USER");
        normalUser.setEnabled(true);
        users.save(normalUser);

        User testUser = new User("testinguser@test.com", "testuser1", "test", "user", encoder.encode("password123"),
                true, "ROLE_USER");
        testUser.setEnabled(true);
        users.save(testUser);
    }

    @AfterAll
    void tearDown() {
        // clear the database after each test
        users.deleteAll();
        vTokens.deleteAll();
    }

    @Test
    public void verificationToken_Invalid_ReturnInvalid() throws Exception {
        URI uriRegistrationConfirmation = new URI(baseUrl + port + "/api/v1/registrationConfirm");

        RequestSpecification request = RestAssured.given();

        Response invalidTokenResponse = request.get(uriRegistrationConfirmation + "?token=1234");

        assertEquals(200, invalidTokenResponse.getStatusCode());
        assertEquals("invalidToken", invalidTokenResponse.getBody().asString());
    }

    @Test
    public void verificationToken_Valid_ReturnValid() throws Exception {
        URI uriRegistrationConfirmation = new URI(baseUrl + port + "/api/v1/registrationConfirm");
        URI uriRegister = new URI(baseUrl + port + "/api/v1/register");

        String newUserDetails = "{\r\n" + "  \"email\": \"testuser6@test.com\",\r\n"
                + "  \"username\": \"testuser6\",\r\n" + "  \"firstName\": \"Test6\",\r\n"
                + "  \"lastName\": \"User\",\r\n" + "  \"password\": \"password123\"\r\n" + "}";

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
}
