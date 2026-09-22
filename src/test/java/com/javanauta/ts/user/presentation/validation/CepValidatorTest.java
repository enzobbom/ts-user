package com.javanauta.ts.user.presentation.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CepValidatorTest {
    private final CepValidator underTest = new CepValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "12345678",
            "12345-678"
    })
    void isValid_shouldReturnTrueForValidCepFormats(String cep) {
        assertThat(underTest.isValid(cep, null))
                .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1234567",
            "123456789",
            "1234-5678",
            "123456-78",
            "abcdefgh",
            "12345-ab"
    })
    void isValid_shouldReturnFalseForInvalidCepFormats(String cep) {
        assertThat(underTest.isValid(cep, null))
                .isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "   "
    })
    void isValid_shouldIgnoreNullOrBlankValues(String cep) {
        assertThat(underTest.isValid(cep, null))
                .isTrue();
    }
}