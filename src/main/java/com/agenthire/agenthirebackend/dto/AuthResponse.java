
package com.agenthire.agenthirebackend.dto;

public class AuthResponse {

    private Long id;
    private String email;
    private String token;
    private String role;

    public AuthResponse(
            Long id,
            String email,
            String token,
            String role
    ) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.role = role;
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

    public String getRole() {
        return role;
    }
}
