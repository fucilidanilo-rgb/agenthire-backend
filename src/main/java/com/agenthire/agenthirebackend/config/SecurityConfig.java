
package com.agenthire.agenthirebackend.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // Abilita CORS utilizzando la configurazione
                // @CrossOrigin dei controller
                .cors(cors -> {})

                // API REST con autenticazione JWT
                .csrf(csrf -> csrf.disable())

                // Nessuna sessione sul server
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Gestione accessi non autorizzati
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                (request, response, authException) -> {
                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );
                                    response.setContentType("application/json");
                                    response.setCharacterEncoding("UTF-8");
                                    response.getWriter().write(
                                            "{\"status\":401,\"error\":\"Unauthorized\"}"
                                    );
                                }
                        )
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {
                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );
                                    response.setContentType("application/json");
                                    response.setCharacterEncoding("UTF-8");
                                    response.getWriter().write(
                                            "{\"status\":403,\"error\":\"Forbidden\"}"
                                    );
                                }
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Consente le richieste preflight CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Login consentito
                        .requestMatchers(
                                HttpMethod.POST, "/api/auth/login"
                        ).permitAll()

                        // Registrazione pubblica bloccata
                        .requestMatchers(
                                HttpMethod.POST, "/api/auth/register"
                        ).denyAll()

                        // Endpoint di verifica
                        .requestMatchers(
                                HttpMethod.GET, "/api/test"
                        ).permitAll()

                        // Lettura candidati: USER e DEMO
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/candidates",
                                "/api/candidates/**"
                        ).hasAnyRole("USER", "DEMO")

                        // Scrittura candidati: solo USER
                        .requestMatchers(
                                "/api/candidates",
                                "/api/candidates/**"
                        ).hasRole("USER")

                        // Tutte le altre API richiedono autenticazione
                        .anyRequest().authenticated()
                )

                // Verifica JWT
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
