package com.javanauta.ts.user.infrastructure.security.authentication;

import java.util.UUID;

public record AuthenticatedPrincipal(
        UUID id,
        String email
) {
}
