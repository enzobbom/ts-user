package com.javanauta.ts.user.presentation.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
        @NotBlank @Email String email,
        @NotNull String password
) {
}
