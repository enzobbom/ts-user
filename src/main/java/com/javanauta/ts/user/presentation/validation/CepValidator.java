package com.javanauta.ts.user.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CepValidator implements ConstraintValidator<Cep, String> {

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null || cep.isBlank()) {
            return true;
        }

        return Pattern.matches("^(\\d{8}|\\d{5}-\\d{3})$", cep);
    }
}