package com.javanauta.ts.user.presentation.dto.in;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record CreateUserAddressDTO(
        String street,
        String number,
        String complement,
        String city,
        String neighbourhood,
        String state,
        String cep)
{}
