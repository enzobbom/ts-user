package com.javanauta.ts.user.presentation.dto.in;

import com.javanauta.ts.user.presentation.validation.Cep;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record CreateUserAddressDTO(
        @NotBlank String street,
        @NotBlank String number,
        String complement,
        @NotBlank String city,
        String neighbourhood,
        @NotBlank String state,
        @NotBlank @Cep String cep)
{}
