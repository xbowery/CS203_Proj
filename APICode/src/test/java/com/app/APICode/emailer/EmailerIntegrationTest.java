// package com.app.APICode.emailer;

// import static org.junit.jupiter.api.Assertions.*;

// import com.app.APICode.user.UserRepository;
// import com.icegreen.greenmail.configuration.GreenMailConfiguration;
// import com.icegreen.greenmail.junit5.GreenMailExtension;
// import com.icegreen.greenmail.util.GreenMail;
// import com.icegreen.greenmail.util.ServerSetupTest;

// import static org.awaitility.Awaitility.await;
// import org.junit.Test;
// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestInstance.Lifecycle;
// import org.junit.jupiter.api.extension.RegisterExtension;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.web.server.LocalServerPort;
// import org.springframework.test.context.ActiveProfiles;

// import io.restassured.RestAssured;
// import io.restassured.config.JsonConfig;
// import io.restassured.path.json.config.JsonPathConfig;
// import io.restassured.response.Response;
// import io.restassured.specification.RequestSpecification;

// import static io.restassured.config.RedirectConfig.redirectConfig;

// import java.net.URI;
// import java.util.concurrent.TimeUnit;

// import javax.mail.internet.MimeMessage;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @ActiveProfiles("test")
// @TestInstance(Lifecycle.PER_CLASS)
// public class EmailerIntegrationTest {
    
//     @LocalServerPort
//     private int port;

//     private final String baseUrl = "http://localhost:";
    
//     @RegisterExtension
//     static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
//     .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
//     .withPerMethodLifecycle(false);

//     @Autowired
//     private UserRepository users;

//     @BeforeAll
//     public static void initClass() {
//         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//         RestAssured.useRelaxedHTTPSValidation();
//         RestAssured.urlEncodingEnabled = false;
//         RestAssured.config = RestAssured.config()
//             .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
//             .redirect(redirectConfig().followRedirects(false));
//     }

//     @BeforeAll
//     void startGreenMail() {
//         greenMail.start();
//     }

//     @AfterAll
// 	void tearDown(){
// 		// clear the database after each test
// 		users.deleteAll();
//         greenMail.stop();
// 	}

//     @Test
//     void sendEmail_CorrectRegisterPayload_User() throws Exception {
//         URI uri = new URI(baseUrl + port + "/api/v1/register");

//         String newUserDetails = "{\r\n" +
//         "  \"email\": \"duke@spring.io\",\r\n" +
//         "  \"username\": \"duketest\",\r\n" +
//         "  \"firstName\": \"Duke\",\r\n" +
//         "  \"lastName\": \"Test\",\r\n" +
//         "  \"password\": \"springboot\"\r\n" +
//         "}";

//         RequestSpecification request = RestAssured.given();

//         request.header("Content-Type", "application/json");

//         Response registerUserResponse = request.body(newUserDetails).post(uri);

//         assertEquals(204, registerUserResponse.getStatusCode());

//         await().atMost(60, TimeUnit.SECONDS).untilAsserted(() -> {
//             MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
//             assertEquals("duke@spring.io", receivedMessage.getAllRecipients()[0].toString());
//             assertEquals(1, receivedMessage.getAllRecipients().length);
//             assertEquals("isRegisterConfirmation", receivedMessage.getSubject());
//         });
//     }
// }
