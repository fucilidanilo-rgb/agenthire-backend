package com.agenthire.agenthirebackend.service;

import com.agenthire.agenthirebackend.dto.AuthResponse;
import com.agenthire.agenthirebackend.dto.LoginRequest;
import com.agenthire.agenthirebackend.dto.RegisterRequest;
import com.agenthire.agenthirebackend.dto.UserResponse;
import com.agenthire.agenthirebackend.entity.AppUser;
import com.agenthire.agenthirebackend.exception.InvalidCredentialsException;
import com.agenthire.agenthirebackend.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) {

        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        AppUser user = new AppUser(
                request.getEmail(),
                encodedPassword
        );

        AppUser savedUser = appUserRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail()
        );
    }

    public AuthResponse login(LoginRequest request) {

        AppUser user = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Email o password non corretti"
                        )
                );

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new InvalidCredentialsException(
                    "Email o password non corretti"
            );
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                token
        );
    }
}