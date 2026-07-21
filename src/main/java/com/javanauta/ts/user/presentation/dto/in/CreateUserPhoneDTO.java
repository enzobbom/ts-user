package com.javanauta.ts.user.presentation.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record CreateUserPhoneDTO(
        @NotBlank String countryCode,
        @NotBlank String number)
{}
