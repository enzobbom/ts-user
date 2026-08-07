package com.javanauta.ts.user.infrastructure.security.exception.enums;

import com.javanauta.ts.user.shared.exception.ExceptionCode;

public enum SecurityExceptionCode implements ExceptionCode {
    AUTHENTICATION_ERROR ("Authentication failed");

    private final String defaultMessage;

    SecurityExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
