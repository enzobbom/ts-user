package com.javanauta.ts.user.presentation.exception;

import com.javanauta.ts.apicontract.response.ErrorResponse;
import com.javanauta.ts.apicontract.response.ValidationErrorDetail;
import com.javanauta.ts.user.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.user.presentation.exception.enums.PresentationExceptionCode;
import com.javanauta.ts.user.shared.exception.ApplicationException;
import com.javanauta.ts.user.shared.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final Map<ExceptionCode, HttpStatus> BUSINESS_CODE_HTTP_STATUS_MAP = Map.of(
            ServiceExceptionCode.USER_ALREADY_EXISTS, HttpStatus.CONFLICT,
            ServiceExceptionCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND,
            ServiceExceptionCode.INEXISTENT_CEP, HttpStatus.NOT_FOUND);

    private static final Map<ExceptionCode, HttpStatus> PRESENTATION_CODE_HTTP_STATUS_MAP = Map.of(
            PresentationExceptionCode.REQUEST_BODY_VIOLATION_ERROR, HttpStatus.UNPROCESSABLE_CONTENT,
            PresentationExceptionCode.PARAM_OR_PATH_VAR_VIOLATION_ERROR, HttpStatus.UNPROCESSABLE_CONTENT,
            PresentationExceptionCode.TYPE_MISMATCH_ERROR, HttpStatus.BAD_REQUEST,
            PresentationExceptionCode.JSON_PARSE_ERROR, HttpStatus.BAD_REQUEST,
            PresentationExceptionCode.MISSING_PARAMETER_ERROR, HttpStatus.BAD_REQUEST,
            PresentationExceptionCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

    // Authentication errors during login attempt

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handlerBadCredentialsException(BadCredentialsException ex) {
        log.warn("Bad credentials exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handlerAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication error");
    }

    //

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
        ExceptionCode exceptionCode = ex.getCode();
        HttpStatus httpCode = BUSINESS_CODE_HTTP_STATUS_MAP.get(exceptionCode);

        List<ValidationErrorDetail> validationErrors = ex.getValidationExceptionDetails()
                .stream()
                .map(validationException -> new ValidationErrorDetail(
                        validationException.code().getExceptionSourceType().getIdentifier(),
                        validationException.source(),
                        validationException.message()))
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                httpCode.value(),
                exceptionCode.getIdentifier(),
                ex.getMessage(),
                validationErrors);

        return ResponseEntity.status(httpCode).body(errorResponse);
    }

    // Generic error handling

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception", ex);

        ExceptionCode exceptionCode = PresentationExceptionCode.INTERNAL_SERVER_ERROR;
        HttpStatus httpCode = PRESENTATION_CODE_HTTP_STATUS_MAP.get(exceptionCode);
        String errorIdentifier = exceptionCode.getIdentifier();

        ErrorResponse errorResponse = new ErrorResponse(
                httpCode.value(),
                errorIdentifier,
                exceptionCode.getDefaultMessage(),
                List.of());

        return ResponseEntity.status(httpCode).body(errorResponse);
    }
}
