package com.javanauta.ts.user.controller.dto.in;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record CreateUserAddressDTO(
        String street,
        Long number,
        String complement,
        String city,
        String neighbourhood,
        String state,
        String cep)
{}
