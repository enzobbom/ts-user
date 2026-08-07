package com.javanauta.ts.user.presentation.dto.in;

import com.javanauta.ts.user.presentation.validation.AtLeastOneField;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@AtLeastOneField
public record UpdateUserPhoneDTO(
        @Pattern(regexp = "^\\d{1,3}$") String countryCode,
        @Pattern(regexp = "^\\d{1,15}$") String number) {
}
