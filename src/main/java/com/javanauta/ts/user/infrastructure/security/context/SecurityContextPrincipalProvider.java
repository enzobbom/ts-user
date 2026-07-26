package com.javanauta.ts.user.infrastructure.security.context;

import com.javanauta.ts.user.application.ports.out.security.PrincipalProvider;
import com.javanauta.ts.user.infrastructure.security.authentication.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityContextPrincipalProvider implements PrincipalProvider {

    @Override
    public UUID getUserId() {
        return getAuthenticatedUser().id();
    }

    @Override
    public String getEmail() {
        return getAuthenticatedUser().email();
    }

    private AuthenticatedPrincipal getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof AuthenticatedPrincipal user)) {
            throw new IllegalStateException("Invalid authenticated principal");
        }

        return user;
    }
}
