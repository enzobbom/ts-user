package com.javanauta.ts.user.application.exception.enums;

import com.javanauta.ts.user.shared.exception.ExceptionCode;

public enum ServiceExceptionCode implements ExceptionCode {
    USER_ALREADY_EXISTS ("User already exists"),
    USER_NOT_FOUND ("User was not found"),
    INEXISTENT_CEP ("CEP requested for lookup does not exist");

    private final String defaultMessage;

    ServiceExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
