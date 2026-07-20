package com.javanauta.ts.user.infrastructure.security;

import com.javanauta.ts.user.application.ports.out.security.PrincipalProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityPrincipalProvider implements PrincipalProvider {

    @Override
    public String getEmail() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {

            throw new IllegalStateException("No authenticated user found");
        }

        return authentication.getName();
    }
}
