package com.javanauta.ts.user.application.data;

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
