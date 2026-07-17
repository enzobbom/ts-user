package com.javanauta.ts.user.controller.dto.in;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record CreateUserPhoneDTO(
        String countryCode,
        String areaCode,
        String number)
{}
