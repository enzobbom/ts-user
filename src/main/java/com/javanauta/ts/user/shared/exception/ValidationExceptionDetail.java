package com.javanauta.ts.user.shared.exception;

public record ValidationExceptionDetail(ValidationExceptionCode code, String source, String message) {

    public ValidationExceptionDetail(ValidationExceptionCode code, String source) {
        this(code, source, code.getDefaultMessage());
    }
}
