package com.gdmn.card_manager.controller;

import com.gdmn.card_manager.dto.UserData;
import com.gdmn.card_manager.model.User;
import com.gdmn.card_manager.repository.UserRepository;
import com.gdmn.card_manager.secure.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${initial.admin.username}")
    private String username;

    @Value("${initial.admin.password}")
    private String password;

    private HttpHeaders headers;

    private String token;
    private User testUser;

    @BeforeEach
    void setUp() {
        token = this.obtainJwtToken(username, password);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        testUser =
                userRepository.save(
                        new User()
                                .setUsername("testUser@gmail.com")
                                .setPassword("testPassword#123")
                                .setFirstName("testFirstName")
                                .setMidlName("testMidlName")
                                .setLastName("testLastName")
                                .setEnabled(true)
                );
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(testUser);
    }

    @Test
    void getUserById_withAdminRole_shouldReturnUserData() {
        headersSetToken(token);
        long userId = testUser.getId();

        ResponseEntity<UserData> response = restTemplate.exchange(
                "/users/" + userId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserData.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(userId);
        assertThat(response.getBody().getEmail()).isEqualTo(testUser.getUsername());
        assertThat(response.getBody().getRole()).isEqualTo(testUser.getRole());
    }

    @Test
    void getCurrentUser_withAuthenticatedUser_shouldReturnUserData() {
        headersSetToken(token);

        ResponseEntity<UserData> response = restTemplate.exchange(
                "/users/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserData.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo(username);
    }

    @Test
    void enableUser_withAdminRole() {
        headersSetToken(token);
        long userId = testUser.getId();
        testUser.setEnabled(false);
        userRepository.save(testUser);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/" + userId + "/enable",
                HttpMethod.PATCH,
                new HttpEntity<>(headers),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void enableUser_whenAlreadyEnabled_shouldReturnConflict() {
        headersSetToken(token);
        long userId = testUser.getId();

        ResponseEntity<?> response = restTemplate.exchange(
                "/users/" + userId + "/enable",
                HttpMethod.PATCH,
                new HttpEntity<>(headers),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void enableUser_withoutAdminRole_shouldReturnForbidden() {
        long userId = testUser.getId();

        ResponseEntity<?> response = restTemplate.exchange(
                "/users/" + userId + "/enable",
                HttpMethod.PATCH,
                new HttpEntity<>(headers),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void disableUser_withAdminRole() {
        headersSetToken(token);
        long userId = testUser.getId();

        ResponseEntity<?> response = restTemplate.exchange(
                "/users/" + userId + "/disable",
                HttpMethod.PATCH,
                new HttpEntity<>(headers),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void disableUser_whenAlreadyDisable_shouldReturnConflict() {
        headersSetToken(token);
        long userId = testUser.getId();
        testUser.setEnabled(false);
        userRepository.save(testUser);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/" + userId + "/disable",
                HttpMethod.PATCH,
                new HttpEntity<>(headers),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void disableUser_withoutAdminRole_shouldReturnForbidden() {
        long userId = testUser.getId();

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/" + userId + "/disable",
                HttpMethod.PATCH,
                new HttpEntity<>(headers),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void deleteUser_withAdminRole() {
        headersSetToken(token);
        long userId = testUser.getId();

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/" + userId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockUser
    void deleteUser_withoutAdminRole_shouldReturnForbidden() {
        long userId = testUser.getId();

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/" + userId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getUser_withoutAuthentication_shouldReturnForbidden() {
        long userId = testUser.getId();

        ResponseEntity<?> response = restTemplate.exchange(
                "/users/" + userId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private String obtainJwtToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtTokenUtil.generateToken(userDetails);
    }

    private void headersSetToken(String token) {
        headers.set("Authorization", "Bearer " + token);
    }
}