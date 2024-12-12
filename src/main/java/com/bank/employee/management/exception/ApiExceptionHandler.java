package com.bank.employee.management.exception;

import com.bank.employee.management.domain.ApiErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiErrorResponse> handleFeignException(final FeignException exception) {
        log.error("FeignException - http status: {}, error message: {}",
                exception.status(), exception.getMessage());
        if(exception.status() == NOT_FOUND.value()) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiErrorResponse(NOT_FOUND.name(), exception.getMessage(), null));
        } else {
            log.error("FeignException - stack trace: {}", getStackTrace(exception));
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiErrorResponse(INTERNAL_SERVER_ERROR.name(), "Something went wrong", null));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.warn("Missing required field - {}", exception.getMessage());
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return ResponseEntity.badRequest().body(processFieldErrors(fieldErrors));
    }

    @ExceptionHandler({InputValidationException.class, MissingRequestHeaderException.class})
    public ResponseEntity<ApiErrorResponse> handleInputValidationException(final Exception exception) {
        log.warn("Input validation exception - {}", exception.getMessage());
        return ResponseEntity.badRequest().body(new ApiErrorResponse(BAD_REQUEST.name(), exception.getMessage(), null));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(final Exception exception) {
        log.error("Internal server error - {}, stack trace: {}", exception.getMessage(), getStackTrace(exception));
        return ResponseEntity.internalServerError().body(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), "Something went wrong", null));
    }

    private ApiErrorResponse processFieldErrors(List<FieldError> fieldErrors) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError: fieldErrors) {
            errors.add(fieldError.getDefaultMessage());
        }
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.name(), "Input validation error", errors);
    }
}