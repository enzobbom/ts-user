package com.javanauta.ts.user.controller.dto.out;

import lombok.*;

@Builder
public record AddressResponseDTO(
        String street,
        Long number,
        String complement,
        String city,
        String neighbourhood,
        String state,
        String cep
) {
}
