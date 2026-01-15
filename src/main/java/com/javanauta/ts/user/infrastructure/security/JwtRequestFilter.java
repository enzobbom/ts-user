package com.javanauta.ts.user.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Defines the JwtRequestFilter class, which extends OncePerRequestFilter
public class JwtRequestFilter extends OncePerRequestFilter {

    // Defines properties to store instances of JwtUtil and UserDetailsService
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Constructor that initializes the properties with provided instances
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Method called once per request to process the filter
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Gets the value of the "Authorization" header from the request
        final String authorizationHeader = request.getHeader("Authorization");

        // Checks whether the header exists and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extracts the JWT token from the header
            final String token = authorizationHeader.substring(7);
            // Extracts the username from the JWT token
            final String username = jwtUtil.extractUsername(token);

            // If the username is not null and the user is not yet authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Loads user details from the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // Validates the JWT token
                if (jwtUtil.validateToken(token, username)) {
                    // Creates an authentication object with user information
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    // Sets authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // Continues the filter chain, allowing the request to proceed
        chain.doFilter(request, response);
    }
}
