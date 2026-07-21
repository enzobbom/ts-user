package com.javanauta.ts.user.presentation.dto.in;

import com.javanauta.ts.user.presentation.validation.AtLeastOneField;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@AtLeastOneField
public record UpdateUserRequestDTO(
    String name,
    @Email String email,
    String password
)
{}

