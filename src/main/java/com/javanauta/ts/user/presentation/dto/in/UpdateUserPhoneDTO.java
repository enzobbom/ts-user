package com.javanauta.ts.user.presentation.dto.in;

import com.javanauta.ts.user.presentation.validation.AtLeastOneField;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@AtLeastOneField
public record UpdateUserPhoneDTO(
        String countryCode,
        String number)
{}
