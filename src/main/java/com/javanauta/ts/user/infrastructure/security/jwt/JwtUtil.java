package com.javanauta.ts.user.infrastructure.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.javanauta.ts.user.domain.model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private SecretKey signingKey;

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${ts.jwt.secret}")
    private String secretKey;

    @Value("${ts.jwt.expiration}")
    private long jwtExpirationMs;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .issuer(issuer)
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtExpirationMs))
                .signWith(signingKey)
                .compact();
    }
}
