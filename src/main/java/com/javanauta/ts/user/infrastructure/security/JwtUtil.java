package com.javanauta.ts.user.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtil {

    private JwtParser jwtParser;
    private SecretKey signingKey;

    // Secret key used to sign and verify JWT tokens
    @Value("${ts.jwt.secret}")
    private String secretKey;

    private static final long JWT_EXPIRATION_MS = 1000 * 60 * 60L;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        jwtParser = Jwts.parser()
                .verifyWith(signingKey)
                .build();
    }

    // Generates a JWT token with the username and 1-hour expiration time
    public String generateToken(String username) {
        Date now = new Date();

        return Jwts.builder()
                .subject(username) // Sets username as the token subject
                .issuedAt(now) // Sets the token issue date/time
                .expiration(new Date(now.getTime() + JWT_EXPIRATION_MS)) // Sets expiration (1 hour from now)
                .signWith(signingKey) // Converts key to bytes and signs the token
                .compact(); // Builds the JWT token
    }

    // Extracts claims from the JWT token
    public Claims extractClaims(String token) {
        return jwtParser
                .parseSignedClaims(token) // Parses the JWT token and gets claims
                .getPayload(); // Returns claims body
    }

    // Extracts username from the JWT token
    public String extractUsername(String token) {
        // Gets the subject (username) from the claims
        return extractClaims(token).getSubject();
    }

    // Checks whether the JWT token is expired
    public boolean isTokenExpired(String token) {
        // Compares token expiration date with current date
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Validates the JWT token by checking username match and expiration
    public boolean validateToken(String token, String username) {
        // Extracts username from the token
        final String extractedUsername = extractUsername(token);
        // Checks if username matches and token is not expired
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
