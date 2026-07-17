package com.javanauta.ts.user.controller.dto.out;

import lombok.*;

@Builder
public record PhoneResponseDTO(
        String countryCode,
        String areaCode,
        String number
) {
}
