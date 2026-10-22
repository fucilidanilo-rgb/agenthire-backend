package com.agenthire.agenthirebackend.controller;

import com.agenthire.agenthirebackend.dto.AuthResponse;
import com.agenthire.agenthirebackend.dto.LoginRequest;
import com.agenthire.agenthirebackend.dto.RegisterRequest;
import com.agenthire.agenthirebackend.dto.UserResponse;
import com.agenthire.agenthirebackend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        UserResponse userResponse = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(authResponse);
    }
}