package com.javanauta.ts.user.controller.dto.in;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    private Long id;
    private String street;
    private Long number;
    private String complement;
    private String city;
    private String neighbourhood;
    private String state;
    private String cep;
}
