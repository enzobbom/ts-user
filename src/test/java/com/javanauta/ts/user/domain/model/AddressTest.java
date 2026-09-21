package com.javanauta.ts.user.domain.model;

import com.javanauta.ts.user.application.data.AddressData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressTest {
    @Test
    void update_shouldUpdateProvidedFieldsAndKeepNullFieldsUnchanged() {
        Address address = new Address(new AddressData(
                "Original Street",
                "123",
                "Apartment 4",
                "City",
                "Neighbourhood",
                "City",
                "12345-678"
        ));

        address.update(new AddressData(
                "Updated Street",
                null,
                null,
                "Other City",
                null,
                null,
                "87654-321"
        ));

        assertThat(address.getStreet()).isEqualTo("Updated Street");
        assertThat(address.getNumber()).isEqualTo("123");
        assertThat(address.getComplement()).isEqualTo("Apartment 4");
        assertThat(address.getCity()).isEqualTo("Other City");
        assertThat(address.getNeighbourhood()).isEqualTo("Neighbourhood");
        assertThat(address.getState()).isEqualTo("City");
        assertThat(address.getCep()).isEqualTo("87654-321");
    }

    @Test
    void update_shouldClearOptionalFieldsWhenBlank() {
        Address address = new Address(new AddressData(
                "Some Street",
                "123",
                "Apartment 4",
                "City",
                "Neighbourhood",
                "City",
                "12345-678"
        ));

        address.update(new AddressData(
                null,
                null,
                "",
                null,
                "   ",
                null,
                null
        ));

        assertThat(address.getComplement()).isNull();
        assertThat(address.getNeighbourhood()).isNull();
    }
}