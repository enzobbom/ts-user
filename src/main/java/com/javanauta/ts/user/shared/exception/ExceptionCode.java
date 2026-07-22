package com.javanauta.ts.user.shared.exception;

public interface ExceptionCode {
    default String getIdentifier() {
        if (this instanceof Enum<?>) {
            return ((Enum<?>) this).name();
        }
        return this.toString();
    }

    String getDefaultMessage();
}
