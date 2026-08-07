package com.javanauta.ts.user.application.data;

import java.util.UUID;

public record AuthenticationResult(
        UUID userId,
        String userEmail,
        String token
) {
}
