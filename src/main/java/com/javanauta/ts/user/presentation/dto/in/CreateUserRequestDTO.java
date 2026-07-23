package com.javanauta.ts.user.presentation.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record CreateUserRequestDTO(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8, max = 100) String password,
    @NotNull CreateUserAddressDTO address,
    @NotNull CreateUserPhoneDTO phone
)
{}

