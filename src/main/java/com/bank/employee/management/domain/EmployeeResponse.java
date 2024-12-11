package com.bank.employee.management.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmployeeResponse(
                        Integer id,
                        String name,
                        @JsonProperty("role_id") int roleId) {
}
