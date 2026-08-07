package com.javanauta.ts.user.shared.exception;

import com.javanauta.ts.user.shared.exception.enums.ValidationExceptionSourceType;

public interface ValidationExceptionCode extends ExceptionCode {
    public ValidationExceptionSourceType getExceptionSourceType();
}
