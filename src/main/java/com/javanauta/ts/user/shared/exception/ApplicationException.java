package com.javanauta.ts.user.shared.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ApplicationException extends RuntimeException {
    private final ExceptionCode code;
    private final String message;
    private final List<ValidationExceptionDetail> validationExceptionDetails;

    public ApplicationException(ExceptionCode code, String message, List<ValidationExceptionDetail> validationExceptionsDetails) {
        this.code = code;
        if (message == null || message.isBlank()) {
            this.message = code.getDefaultMessage();
        } else {
            this.message = message;
        }
        if (validationExceptionsDetails == null) {
            this.validationExceptionDetails = List.of();
        } else {
            this.validationExceptionDetails = List.copyOf(validationExceptionsDetails);
        }
    }

    public ApplicationException(ExceptionCode code) {
        this(code, "", null);
    }

    public ApplicationException(ExceptionCode code, List<ValidationExceptionDetail> validationExceptionsDetails) {
        this(code, "", validationExceptionsDetails);
    }

    public ApplicationException(ExceptionCode code, String message) {
        this(code, message, null);
    }
}
