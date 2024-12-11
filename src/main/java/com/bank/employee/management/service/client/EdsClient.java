package com.bank.employee.management.service.client;

import com.bank.employee.management.domain.EdsEmployeeRequest;
import com.bank.employee.management.domain.RoleResponse;
import com.bank.employee.management.domain.EmployeeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "edsClient", url = "http://localhost:8081", configuration = EdsClientConfig.class)
public interface EdsClient {

    @PostMapping(value = "/api/employees", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EmployeeResponse> createEmployee(
            @RequestBody final EdsEmployeeRequest employeeRequest);

    @GetMapping(value = "/api/employees/{id}")
    ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable final Integer employeeId);

    @PutMapping(value = "/api/employees/{id}")
    ResponseEntity<EmployeeResponse> updateEmployee(
            @RequestBody final EdsEmployeeRequest employeeRequest,
            @PathVariable final Integer employeeId
            );

    @DeleteMapping(value = "/api/employees/{id}")
    ResponseEntity<String> deleteEmployeeById(
            @PathVariable final Integer employeeId);

    @GetMapping(value = "/api/roles")
    ResponseEntity<RoleResponse> getRoleByName(
            @RequestParam("name") final String roleName);
}