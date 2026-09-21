package com.javanauta.ts.user.domain.model;

import com.javanauta.ts.user.application.data.AddressData;
import com.javanauta.ts.user.application.data.CreateUserData;
import com.javanauta.ts.user.application.data.PhoneData;
import com.javanauta.ts.user.application.data.UpdateUserData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    void create_shouldCreateUserAndAssignAddressAndPhone() {
        AddressData addressData = new AddressData(
                "Some Street",
                "123",
                "Apartment 4",
                "City",
                "Neighbourhood",
                "State",
                "12345-678"
        );

        PhoneData phoneData = new PhoneData(
                "111",
                "123456789"
        );

        CreateUserData createUserData = new CreateUserData(
                "Test User",
                "user@example.com",
                "encoded-password",
                addressData,
                phoneData
        );

        User user = User.create(createUserData);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getEmail()).isEqualTo("user@example.com");
        assertThat(user.getPassword()).isEqualTo("encoded-password");

        assertThat(user.getAddress()).isNotNull();
        assertThat(user.getAddress().getStreet()).isEqualTo("Some Street");
        assertThat(user.getAddress().getUser()).isSameAs(user);

        assertThat(user.getPhone()).isNotNull();
        assertThat(user.getPhone().getCountryCode()).isEqualTo("111");
        assertThat(user.getPhone().getUser()).isSameAs(user);
    }

    @Test
    void update_shouldUpdateProvidedFieldsAndKeepNullFieldsUnchanged() {
        User user = User.create(new CreateUserData(
                "Original Name",
                "original@example.com",
                "original-password",
                new AddressData(
                        "Some Street",
                        "123",
                        null,
                        "City",
                        null,
                        "City",
                        "12345-678"
                ),
                new PhoneData(
                        "111",
                        "123456789"
                )
        ));

        user.update(new UpdateUserData(
                "Updated Name",
                null,
                "updated-password"
        ));

        assertThat(user.getName()).isEqualTo("Updated Name");
        assertThat(user.getEmail()).isEqualTo("original@example.com");
        assertThat(user.getPassword()).isEqualTo("updated-password");
    }
}