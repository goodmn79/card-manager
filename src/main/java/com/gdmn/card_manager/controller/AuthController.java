package com.gdmn.card_manager.controller;

import com.gdmn.card_manager.dto.AuthenticationRequest;
import com.gdmn.card_manager.dto.Register;
import com.gdmn.card_manager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(tags = {"Registration"},
            summary = "User registration")
    @ApiResponse(responseCode = "201", description = "successfully created")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Register register) {
        if (authService.registerNewUser(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(tags = {"Authorisation"},
            summary = "User authorisation")
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest,
                                                       HttpServletResponse response) {
        String jwt = authService.authenticate(authenticationRequest);

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().body(Map.of("jwt", jwt));
    }
}
