package com.javanauta.ts.user.domain.model;

import com.javanauta.ts.user.application.data.PhoneData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneTest {
    @Test
    void update_shouldUpdateProvidedFieldsAndKeepNullFieldsUnchanged() {
        Phone phone = new Phone(new PhoneData(
                "111",
                "123456789"
        ));

        phone.update(new PhoneData(
                "123",
                null
        ));

        assertThat(phone.getCountryCode()).isEqualTo("123");
        assertThat(phone.getNumber()).isEqualTo("123456789");
    }
}