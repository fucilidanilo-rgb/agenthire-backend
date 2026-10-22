package com.agenthire.agenthirebackend.dto;

public class AuthResponse {

    private Long id;
    private String email;
    private String token;

    public AuthResponse(Long id, String email, String token) {
        this.id = id;
        this.email = email;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}