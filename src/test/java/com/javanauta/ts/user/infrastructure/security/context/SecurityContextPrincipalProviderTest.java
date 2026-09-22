package com.javanauta.ts.user.infrastructure.security.context;

import com.javanauta.ts.user.infrastructure.security.authentication.AuthenticatedPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecurityContextPrincipalProviderTest {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USER_EMAIL = "user@example.com";

    private final SecurityContextPrincipalProvider underTest = new SecurityContextPrincipalProvider();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getIdAndEmail_shouldReturnAuthenticatedPrincipalData() {
        AuthenticatedPrincipal principal =
                new AuthenticatedPrincipal(
                        USER_ID,
                        USER_EMAIL
                );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        Collections.emptyList()
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        assertThat(underTest.getId()).isEqualTo(USER_ID);
        assertThat(underTest.getEmail()).isEqualTo(USER_EMAIL);
    }

    @Test
    void getId_shouldThrowWhenAuthenticationDoesNotExist() {
        assertThatThrownBy(underTest::getId)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No authenticated user found");
    }

    @Test
    void getId_shouldThrowWhenAuthenticationIsNotAuthenticated() {
        AuthenticatedPrincipal principal =
                new AuthenticatedPrincipal(
                        USER_ID,
                        USER_EMAIL
                );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        assertThat(authentication.isAuthenticated()).isFalse();

        assertThatThrownBy(underTest::getId)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No authenticated user found");
    }

    @Test
    void getId_shouldThrowWhenPrincipalHasInvalidType() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "invalid-principal",
                        null,
                        Collections.emptyList()
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        assertThatThrownBy(underTest::getId)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Invalid authenticated principal");
    }
}
