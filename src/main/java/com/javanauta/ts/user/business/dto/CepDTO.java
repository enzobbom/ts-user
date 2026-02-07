package com.javanauta.ts.user.business.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CepDTO {

    private String street;
    private String city;
    private String neighbourhood;
    private String state;
    private String cep;
}
