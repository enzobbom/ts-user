package com.javanauta.ts.user.presentation.dto.out;

import lombok.Builder;

@Builder
public record AddressCepLookupResponseDTO(
        String street,
        String city,
        String neighbourhood,
        String state,
        String cep
) {
}
