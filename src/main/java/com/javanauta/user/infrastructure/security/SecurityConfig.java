package com.javanauta.user.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // JwtUtil and UserDetailsService instances injected by Spring
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Constructor for dependency injection of JwtUtil and UserDetailsService
    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Security filter configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Creates a JwtRequestFilter instance using JwtUtil and UserDetailsService
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection for REST APIs (not needed in stateless APIs)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/login").permitAll() // Allows access to login endpoint
                        .requestMatchers(HttpMethod.GET, "/auth").permitAll() // Allows access to GET /auth endpoint
                        .requestMatchers(HttpMethod.POST, "/user").permitAll() // Allows access to POST /user endpoint
                        .requestMatchers("/user/**").authenticated() // Requires authentication for any /user/** endpoint
                        .anyRequest().authenticated() // Requires authentication for all other requests
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sets session policy to stateless
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Adds JWT filter before default authentication filter

        // Returns the built security configuration
        return http.build();
    }

    // Configures PasswordEncoder using BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Returns BCryptPasswordEncoder instance
    }

    // Configures AuthenticationManager using AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Gets and returns the AuthenticationManager
        return authenticationConfiguration.getAuthenticationManager();
    }

}
