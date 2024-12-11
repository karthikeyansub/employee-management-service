package com.bank.employee.management.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(String errorTitle,
                               String errorMessage,
                               List<String> errorDetails) {
}
