package com.bank.employee.management.exception;

import com.bank.employee.management.domain.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(final EmployeeNotFoundException exception) {
        log.error("Resource not found exception - {}, stack trace: {}", exception.getMessage(), getStackTrace(exception));
        return ResponseEntity.status(NOT_FOUND).body(new ApiErrorResponse(NOT_FOUND.name(), exception.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.warn("Missing required field - {}", exception.getMessage());
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return ResponseEntity.badRequest().body(processFieldErrors(fieldErrors));
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