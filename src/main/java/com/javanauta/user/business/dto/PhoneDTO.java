package com.javanauta.user.business.dto;

import lombok.*;
import java.util.List;

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
