package com.app.APICode.measure;

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

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.app.APICode.templates.LoginDetails;
import com.app.APICode.templates.TokenDetails;
import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class MeasureIntegrationTest {
    
    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private UserRepository users;

    @Autowired
	private BCryptPasswordEncoder encoder;

    @Autowired
    private MeasureRepository measures;

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

        Measure tempMeasure = new Measure("gym", 50, true, false);
        measures.save(tempMeasure);

        Measure testMeasure = new Measure("test", 50, true, false);
        measures.save(testMeasure);
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
        measures.deleteAll();
	}

    @Test
    public void getAllMeasures_AdminUser_Success() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        Response measureListResponse = request.get(uriMeasures);

        List<String> resultMeasureType = new ArrayList<>();
        resultMeasureType.add("gym");
        resultMeasureType.add("test");
        resultMeasureType.add("Office");

        assertEquals(200, measureListResponse.getStatusCode());
        assertEquals(resultMeasureType, JsonPath.from(measureListResponse.getBody().asString()).get("measureType"));
    }

    @Test
    public void getAllMeasures_NormalUser_Success() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response measureListResponse = request.get(uriMeasures);

        List<String> resultMeasureType = new ArrayList<>();
        resultMeasureType.add("gym");
        resultMeasureType.add("test");

        assertEquals(200, measureListResponse.getStatusCode());
        assertEquals(resultMeasureType, JsonPath.from(measureListResponse.getBody().asString()).get("measureType"));
    }

    @Test
    public void getExistentMeasure_AdminUser_Success() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        Response measureResponse = request.get(uriMeasures + "/gym");

        assertEquals(200, measureResponse.getStatusCode());
        assertEquals("gym", JsonPath.from(measureResponse.getBody().asString()).get("measureType"));
    }

    @Test
    public void getExistentMeasure_NormalUser_Success() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response measureResponse = request.get(uriMeasures + "/gym");

        assertEquals(200, measureResponse.getStatusCode());
        assertEquals("gym", JsonPath.from(measureResponse.getBody().asString()).get("measureType"));
    }

    @Test
    public void getNonExistentMeasure_AdminUser_Failure() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        Response measureResponse = request.get(uriMeasures + "/invalid");

        assertEquals(404, measureResponse.getStatusCode());
    }

    @Test
    public void getNonExistentMeasure_NormalUser_Failure() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response measureResponse = request.get(uriMeasures + "/invalid");

        assertEquals(404, measureResponse.getStatusCode());
    }

    @Test
    public void addNewMeasures_AdminUser_Success() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        LocalDate currentDate = LocalDate.now();

        String addMeasureDetails = "{\r\n" +
        "  \"dateUpdated\": \"" + currentDate + "\",\r\n" +
        "  \"measureType\": \"Office\",\r\n" +
        "  \"maxCapacity\": 40,\r\n" +
        "  \"vaccinationStatus\": false,\r\n" +
        "  \"maskStatus\": true\r\n" +
        "}";

        Response addMeasureResponse = request.body(addMeasureDetails).post(uriMeasures);

        assertEquals(201, addMeasureResponse.getStatusCode());
        assertEquals("Office", JsonPath.from(addMeasureResponse.getBody().asString()).get("measureType"));
    }

    @Test
    public void addNewMeasures_NormalUser_Failure() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        LocalDate currentDate = LocalDate.now();

        String addMeasureDetails = "{\r\n" +
        "  \"dateUpdated\": \"" + currentDate + "\",\r\n" +
        "  \"measureType\": \"Office\",\r\n" +
        "  \"maxCapacity\": 40,\r\n" +
        "  \"vaccinationStatus\": false,\r\n" +
        "  \"maskStatus\": true\r\n" +
        "}";

        Response addMeasureResponse = request.body(addMeasureDetails).post(uriMeasures);

        assertEquals(403, addMeasureResponse.getStatusCode());
    }

    @Test
    public void addDuplicateMeasure_AdminUser_Failure() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        LocalDate currentDate = LocalDate.now();

        String addMeasureDetails = "{\r\n" +
        "  \"dateUpdated\": \"" + currentDate + "\",\r\n" +
        "  \"measureType\": \"gym\",\r\n" +
        "  \"maxCapacity\": 40,\r\n" +
        "  \"vaccinationStatus\": false,\r\n" +
        "  \"maskStatus\": true\r\n" +
        "}";

        Response addMeasureResponse = request.body(addMeasureDetails).post(uriMeasures);

        assertEquals(409, addMeasureResponse.getStatusCode());
    }

    @Test
    public void updateExistingMeasure_AdminUser_Success() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        String updateMeasureDetails = "{\r\n" +
        "  \"dateUpdated\": \"" + LocalDate.now() + "\",\r\n" +
        "  \"measureType\": \"gym\",\r\n" +
        "  \"maxCapacity\": 40,\r\n" +
        "  \"vaccinationStatus\": true,\r\n" +
        "  \"maskStatus\": true\r\n" +
        "}";

        Response updateMeasureResponse = request.body(updateMeasureDetails).put(uriMeasures);

        assertEquals(200, updateMeasureResponse.getStatusCode());
        assertEquals("gym", JsonPath.from(updateMeasureResponse.getBody().asString()).get("measureType"));
    }

    @Test
    public void updateExistingMeasure_NormalUser_Failure() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        String updateMeasureDetails = "{\r\n" +
        "  \"dateUpdated\": \"" + LocalDate.now() + "\",\r\n" +
        "  \"measureType\": \"gym\",\r\n" +
        "  \"maxCapacity\": 40,\r\n" +
        "  \"vaccinationStatus\": true,\r\n" +
        "  \"maskStatus\": true\r\n" +
        "}";

        Response updateMeasureResponse = request.body(updateMeasureDetails).put(uriMeasures);

        assertEquals(403, updateMeasureResponse.getStatusCode());
    }

    @Test
    public void updateNonExistingMeasure_AdminUser_Success() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        String updateMeasureDetails = "{\r\n" +
        "  \"dateUpdated\": \"" + LocalDate.now() + "\",\r\n" +
        "  \"measureType\": \"invalidMeasure\",\r\n" +
        "  \"maxCapacity\": 40,\r\n" +
        "  \"vaccinationStatus\": true,\r\n" +
        "  \"maskStatus\": true\r\n" +
        "}";

        Response updateMeasureResponse = request.body(updateMeasureDetails).put(uriMeasures);

        assertEquals(404, updateMeasureResponse.getStatusCode());
    }

    @Test
    public void updateNonExistingMeasure_NormalUser_Failure() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        String updateMeasureDetails = "{\r\n" +
        "  \"dateUpdated\": \"" + LocalDate.now() + "\",\r\n" +
        "  \"measureType\": \"invalidMeasure\",\r\n" +
        "  \"maxCapacity\": 40,\r\n" +
        "  \"vaccinationStatus\": true,\r\n" +
        "  \"maskStatus\": true\r\n" +
        "}";

        Response updateMeasureResponse = request.body(updateMeasureDetails).put(uriMeasures);

        assertEquals(403, updateMeasureResponse.getStatusCode());
    }

    @Test
    public void deleteExistingMeasure_NormalUser_Failure() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedUser).header("Content-Type", "application/json");

        Response deleteMeasureResponse = request.delete(uriMeasures + "/test");

        assertEquals(403, deleteMeasureResponse.getStatusCode());
    }

    @Test
    public void deleteExistingMeasure_AdminUser_Success() throws Exception {
        URI uriMeasures = new URI(baseUrl + port + "/api/v1/measures");

        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + tokenGeneratedAdmin).header("Content-Type", "application/json");

        Response deleteMeasureResponse = request.delete(uriMeasures + "/test");

        assertEquals(200, deleteMeasureResponse.getStatusCode());
    }
}
