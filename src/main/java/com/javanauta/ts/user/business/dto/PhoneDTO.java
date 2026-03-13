package com.javanauta.ts.user.business.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDTO {

    private Long id;
    private String countryCode;
    private String areaCode;
    private String number;
}
