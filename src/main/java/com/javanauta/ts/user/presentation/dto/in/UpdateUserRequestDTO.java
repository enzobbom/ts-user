package com.javanauta.ts.user.presentation.dto.in;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record UpdateUserRequestDTO(
    String name,
    String email,
    String password
)
{}

