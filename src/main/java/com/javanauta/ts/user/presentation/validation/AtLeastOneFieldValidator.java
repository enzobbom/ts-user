package com.javanauta.ts.user.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.RecordComponent;

public class AtLeastOneFieldValidator implements ConstraintValidator<AtLeastOneField, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return false;
        }

        try {
            for (RecordComponent component : obj.getClass().getRecordComponents()) {
                Object value = component.getAccessor().invoke(obj);

                if (value != null) {
                    return true;
                }
            }

            return false;

        } catch (ReflectiveOperationException e) {
            return false;
        }
    }
}