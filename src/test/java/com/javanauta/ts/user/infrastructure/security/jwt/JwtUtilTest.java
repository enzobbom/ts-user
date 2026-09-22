package com.javanauta.ts.user.infrastructure.security.jwt;

import com.javanauta.ts.user.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USER_EMAIL = "user@example.com";

    private static final String ISSUER = "ts-user-test";
    private static final String SECRET = "test-jwt-secret-that-is-definitely-long-enough";
    private static final long EXPIRATION_MS = 86_400_000L;

    private JwtUtil underTest;

    @BeforeEach
    void setUp() {
        underTest = new JwtUtil();

        ReflectionTestUtils.setField(
                underTest,
                "issuer",
                ISSUER
        );

        ReflectionTestUtils.setField(
                underTest,
                "secretKey",
                SECRET
        );

        ReflectionTestUtils.setField(
                underTest,
                "jwtExpirationMs",
                EXPIRATION_MS
        );

        underTest.init();
    }

    @Test
    void generateToken_shouldGenerateSignedTokenWithExpectedClaimsAndExpiration() {
        User user = User.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .build();

        String token = underTest.generateToken(user);

        SecretKey verificationKey = Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );

        Claims claims = Jwts.parser()
                .verifyWith(verificationKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getIssuer()).isEqualTo(ISSUER);
        assertThat(claims.getSubject()).isEqualTo(USER_ID.toString());
        assertThat(claims.get("email", String.class)).isEqualTo(USER_EMAIL);

        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isNotNull();

        assertThat(
                claims.getExpiration().getTime()
                        - claims.getIssuedAt().getTime()
        ).isEqualTo(EXPIRATION_MS);
    }
}