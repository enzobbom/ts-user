package com.javanauta.ts.user.presentation.dto.out;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String password,
        AddressResponseDTO address,
        PhoneResponseDTO phone
) {
}
