package com.app.APICode.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;

import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class JwtIntegrationTest {
    
    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @BeforeAll
    void setupDB() {
        String email = "admin@test.com";
        String password = "goodpassword";
        User user = new User(email, "admin", "admin1", null, encoder.encode(password), true, "ROLE_ADMIN");
        user.setEnabled(true);
        user = userRepository.save(user);
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void login_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/login");
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uri,
                new LoginDetails("admin", "goodpassword"), TokenDetails.class);
        System.out.println(result.getBody().getAccessToken());
        assertEquals(200, result.getStatusCode().value());
        assertEquals("admin", result.getBody().getUsername());
        assertNull(result.getBody().getMessage());
    }
    
    @Test
    void login_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/login");
        ResponseEntity<TokenDetails> result = restTemplate.postForEntity(uri, new LoginDetails("admin", "password"), TokenDetails.class);

        assertEquals(401, result.getStatusCode().value());
        assertNull(result.getBody().getUsername());
        assertNull(result.getBody().getAccessToken());
    }
}
