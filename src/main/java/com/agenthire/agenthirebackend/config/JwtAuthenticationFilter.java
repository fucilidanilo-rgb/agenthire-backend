
package com.agenthire.agenthirebackend.config;

import com.agenthire.agenthirebackend.entity.AppUser;
import com.agenthire.agenthirebackend.repository.AppUserRepository;
import com.agenthire.agenthirebackend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            AppUserRepository appUserRepository
    ) {
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Se manca il token, Spring Security gestira
        // l'accesso agli endpoint protetti.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // Controlla firma e scadenza del JWT.
        if (!jwtService.isTokenValid(token)) {
            unauthorized(response, "Token non valido o scaduto");
            return;
        }

        // Estrae l'email dal token.
        String email = jwtService.extractEmail(token);

        // Verifica che l'utente esista ancora nel database.
        AppUser user = appUserRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            unauthorized(response, "Utente non autorizzato");
            return;
        }

        // Legge il ruolo effettivo dal database.
        String role = user.getRole();

        if (!"USER".equals(role) && !"DEMO".equals(role)) {
            unauthorized(response, "Ruolo non autorizzato");
            return;
        }

        // Registra l'utente autenticato in Spring Security.
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        List.of(
                                new SimpleGrantedAuthority("ROLE_" + role)
                        )
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void unauthorized(
            HttpServletResponse response,
            String message
    ) throws IOException {

        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\""
                        + message + "\"}"
        );
    }
}
