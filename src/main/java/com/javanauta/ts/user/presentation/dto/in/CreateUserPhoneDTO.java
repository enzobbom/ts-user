package com.javanauta.ts.user.presentation.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record CreateUserPhoneDTO(
        @NotBlank @Pattern(regexp = "^\\d{1,3}$") String countryCode,
        @NotBlank @Pattern(regexp = "^\\d{1,15}$") String number)
{}
