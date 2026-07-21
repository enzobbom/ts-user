package com.javanauta.ts.user.presentation.dto.in;

import com.javanauta.ts.user.presentation.validation.AtLeastOneField;
import com.javanauta.ts.user.presentation.validation.Cep;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@AtLeastOneField
public record UpdateUserAddressDTO(
        String street,
        String number,
        String complement,
        String city,
        String neighbourhood,
        String state,
        @Cep String cep)
{}
