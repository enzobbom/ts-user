package com.javanauta.ts.user.presentation.dto.out;

import lombok.*;

@Builder
public record AddressResponseDTO(
        String street,
        String number,
        String complement,
        String city,
        String neighbourhood,
        String state,
        String cep
) {
}
