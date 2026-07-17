package com.javanauta.ts.user.presentation.dto.in;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record CreateUserRequestDTO(
    String name,
    String email,
    String password,
    CreateUserAddressDTO address,
    CreateUserPhoneDTO phone
)
{}

