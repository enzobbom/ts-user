package com.javanauta.ts.user.application.data;

public record AddressCepLookupData(
        String street,
        String city,
        String neighbourhood,
        String state,
        String cep
) {
}
