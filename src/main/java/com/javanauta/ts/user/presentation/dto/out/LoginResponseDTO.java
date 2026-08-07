package com.javanauta.ts.user.presentation.dto.out;

import java.util.UUID;

public record LoginResponseDTO(
        UUID userId,
        String userEmail,
        String token
) {
}
