package com.javanauta.ts.user.domain.data;

public record AddressData(
        String street,
        Long number,
        String complement,
        String city,
        String neighbourhood,
        String state,
        String cep
) {
}
