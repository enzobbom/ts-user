package com.javanauta.ts.user.shared.exception.enums;

public enum ValidationExceptionSourceType {
    OBJECT,     // class-level (cross-field) validation, e.g., @Valid on a class
    FIELD,      // field-level validation of an object, e.g., @NotNull on a class attribute
    PARAMETER;  // method parameter-level validation, e.g., @NotNull on a method parameter

    public String getIdentifier() {
        return this.name();
    }
}
