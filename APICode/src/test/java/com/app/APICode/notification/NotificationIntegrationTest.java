package com.app.APICode.notification;

import static io.restassured.RestAssured.given;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;

import java.net.URI;

import javax.transaction.Transactional;

import com.app.APICode.security.jwt.JWTHelper;
import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class NotificationIntegrationTest {
    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private NotificationRepository notifications;

    @Autowired
    private UserRepository users;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private static RequestSpecification request;
    private User admin;

    @BeforeAll
    public static void initClass() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config()
                .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
                .redirect(redirectConfig().followRedirects(false));
    }

    /**
     * Set up the DB with a user. This user will be the main target for adding,
     * retrieving and obtaining notifications. Also, it is in charge of obtaining a
     * JWT string for API calls later.
     */
    @BeforeAll
    void setupDB() {
        admin = new User("admin@test.com", "admin", "admin1", null, encoder.encode("goodpassword"), true, "ROLE_ADMIN");
        admin.setEnabled(true);
        admin = users.save(admin);

        String tokenGeneratedAdmin = JWTHelper.generateAccessToken(admin);

        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", "Bearer " + tokenGeneratedAdmin);
        builder.addHeader("Content-Type", "application/json");

        request = builder.build();
    }

    // clear the database after each of the test
    @AfterEach
    void tearDownNotif() {
        notifications.deleteAll();
    }

    @AfterAll
    void tearDownUser() {
        users.deleteAll();
    }

    /**
     * Initially the database is empty, so a user will not receive any notification
     * 
     * @throws Exception
     */
    @Test
    void getAllNotification_NewUser_ReturnsEmptyList() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/notifications");
        given().spec(request).get(uri).then().statusCode(200).body("isEmpty()", is(true));
    }

    /**
     * After notifications are created, the API should fetch the unread
     * notifications
     * 
     * @throws Exception
     */
    @Test
    void getAllNotification_User_ReturnsUnreadNotifications() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/notifications");

        int id = notifications.save(new Notification("New notification 1", admin)).getId().intValue();
        int id2 = notifications.save(new Notification("New notification 2", admin)).getId().intValue();

        given().spec(request).get(uri).then().statusCode(200).body("size()", equalTo(2))
                .body("text", containsInAnyOrder("New notification 1", "New notification 2"))
                .body("id", containsInAnyOrder(id, id2)).body("seen", everyItem(is(false)));
    }

    /**
     * Insert and make the notification read
     * 
     * @throws Exception
     */
    @Test
    void readSingleNotification_ExistingNotification_ReturnsSuccess() throws Exception {

        Long id = notifications.save(new Notification("New notification 1", admin)).getId();
        URI uri = new URI(baseUrl + port + "/api/v1/notifications/" + id);

        given().spec(request).put(uri).then().assertThat().statusCode(200);

    }

    /**
     * Non Existing Notification ID throws error
     * 
     * @throws Exception
     */
    @Test
    void readSingleNotification_NonExistingNotification_ReturnsError() throws Exception {

        Long nonExistId = 1337L;
        URI uri = new URI(baseUrl + port + "/api/v1/notifications/" + nonExistId);

        given().spec(request).put(uri).then().assertThat().statusCode(404);

    }

    /**
     * Wrong notification - user pair returns error
     * 
     * @throws Exception
     */
    @Test
    void readSingleNotification_WrongNotificationUserPair_ReturnsError() throws Exception {
        User anotherUser = new User("another@test.com", "anotherUser", "user", null, encoder.encode("goodpassword"),
                true, "ROLE_ADMIN");
        anotherUser.setEnabled(true);
        users.save(anotherUser);

        int id = notifications.save(new Notification("New notification 1", anotherUser)).getId().intValue();
        URI uri = new URI(baseUrl + port + "/api/v1/notifications/" + id);

        given().spec(request).put(uri).then().assertThat().statusCode(404);

    }

    /**
     * Read all notification returns success
     * 
     * @throws Exception
     */
    @Test
    @Transactional
    void readAllNotification_User_ReturnsSuccess() throws Exception {

        notifications.save(new Notification("New notification 1", admin));
        notifications.save(new Notification("New notification 2", admin));

        URI uri = new URI(baseUrl + port + "/api/v1/notifications/all");

        given().log().all().spec(request).put(uri).then().assertThat().statusCode(200);

    }

}
