package com.javanauta.user.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtil {

    // Secret key used to sign and verify JWT tokens
    private final String secretKey = "sua-chave-secreta-super-segura-que-deve-ser-bem-longa";

    // Generates a JWT token with the username and 1-hour expiration time
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Sets username as the token subject
                .setIssuedAt(new Date()) // Sets the token issue date/time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Sets expiration (1 hour from now)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256) // Converts key to bytes and signs the token
                .compact(); // Builds the JWT token
    }

    // Extracts claims from the JWT token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))) // Sets secret key to validate signature
                .build()
                .parseClaimsJws(token) // Parses the JWT token and gets claims
                .getBody(); // Returns claims body
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
