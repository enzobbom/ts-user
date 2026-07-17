package com.javanauta.ts.user.controller.dto.out;

import lombok.Builder;

@Builder
public record UserResponseDTO(
        String name,
        String email,
        String password,
        AddressResponseDTO address,
        PhoneResponseDTO phone
) {
}
