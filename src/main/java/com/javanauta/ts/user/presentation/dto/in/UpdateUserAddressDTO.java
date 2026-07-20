package com.javanauta.ts.user.presentation.dto.in;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record UpdateUserAddressDTO(
        String street,
        Long number,
        String complement,
        String city,
        String neighbourhood,
        String state,
        String cep)
{}
