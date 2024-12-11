package com.bank.employee.management.api;

import ch.qos.logback.core.util.StringUtil;
import com.bank.employee.management.domain.EmployeeRequest;
import com.bank.employee.management.exception.InputValidationException;
import com.bank.employee.management.service.EmployeeService;
import com.bank.employee.management.service.RoleService;
import com.bank.employee.management.domain.EmployeeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/employees")
@AllArgsConstructor
@Validated
public class EmployeeController {

    private EmployeeService employeeService;

    private RoleService roleService;

    @Operation(summary = "Create new employee",
            description = " This API will create new employee and return employee details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns employee details"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "500", description = "System errors")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeResponse createEmployee(
            @RequestHeader("role") final String role,
            @Valid @RequestBody final EmployeeRequest employeeRequest) {
        log.info("Received request to create /employees");
        if(StringUtil.isNullOrEmpty(role)) {
            throw new InputValidationException("Role is missing in the header");
        }

        Integer roleId = roleService.getRoleIdByName(role);
        employeeRequest.setRoleId(roleId);

        return employeeService.createEmployee(employeeRequest);
    }

    @Operation(summary = "Get employee details by id",
            description = " This API will return employee details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns employee details"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "System errors")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public EmployeeResponse getEmployeeById(
            @Parameter(name = "id", description = "Employee Id") @PathVariable("id") final int employeeId) {
        log.info("Received request to get /employees/{}", employeeId);

        return employeeService.getEmployeeById(employeeId);
    }

    @Operation(summary = "Update employee",
            description = " This API will update employee and return employee details with the updated information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns employee details"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "System errors")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public EmployeeResponse updateEmployee(
            @Parameter(name = "id", description = "Employee Id") @PathVariable("id") final int employeeId,
            @RequestHeader("role") final String role,
            @Valid @RequestBody final EmployeeRequest employeeRequest) {
        log.info("Received request to update /employees/{}", employeeId);

        if(StringUtil.isNullOrEmpty(role)) {
            throw new InputValidationException("Role is missing in the header");
        }

        Integer roleId = roleService.getRoleIdByName(role);
        employeeRequest.setRoleId(roleId);

        return employeeService.updateEmployee(employeeId, employeeRequest);
    }

    @Operation(summary = "Delete employee",
            description = " This API will delete employee and return message in the response.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns employee deleted message"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "System errors")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteEmployee(
            @Parameter(name = "id", description = "Employee Id") @PathVariable("id") final int employeeId) {
        log.info("Received request to delete /employees/{}", employeeId);

        return employeeService.deleteEmployeeById(employeeId);
    }
}