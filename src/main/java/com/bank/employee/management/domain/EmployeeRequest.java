package com.bank.employee.management.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public final class EmployeeRequest
        implements Serializable {

        @NotNull(message = "The field 'first_name' should not be null")
        @NotBlank(message = "The field 'first_name' should not be empty")
        @JsonProperty("first_name")
        private String firstname;

        @NotNull(message = "The field 'sur_name' should not be null")
        @NotBlank(message = "The field 'sur_name' should not be empty")
        @JsonProperty("sur_name")
        private String surname;

        @JsonIgnore
        private Integer roleId;
}
