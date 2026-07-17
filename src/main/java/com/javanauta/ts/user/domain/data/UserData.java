package com.javanauta.ts.user.domain.data;

public record UserData(
        String name,
        String email,
        String password,
        AddressData addressData,
        PhoneData phoneData) {
}
