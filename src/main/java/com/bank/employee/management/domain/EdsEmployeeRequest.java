package com.bank.employee.management.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record EdsEmployeeRequest(
                        String name,
                        @JsonProperty("role_id") int roleId) implements Serializable {
}
