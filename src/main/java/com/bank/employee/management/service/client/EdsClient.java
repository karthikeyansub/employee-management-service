package com.bank.employee.management.service.client;

import com.bank.employee.management.domain.ApiSuccessResponse;
import com.bank.employee.management.domain.EdsEmployeeRequest;
import com.bank.employee.management.domain.RoleResponse;
import com.bank.employee.management.domain.EmployeeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "edsClient", url = "${eds.url}", configuration = EdsClientConfig.class)
public interface EdsClient {

    @PostMapping(value = "/api/employees", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EmployeeResponse> createEmployee(
            @RequestBody final EdsEmployeeRequest employeeRequest);

    @GetMapping(value = "/api/employees/{id}")
    ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable final Integer id);

    @PutMapping(value = "/api/employees/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable final Integer id,
            @RequestBody final EdsEmployeeRequest employeeRequest);

    @DeleteMapping(value = "/api/employees/{id}")
    ResponseEntity<ApiSuccessResponse> deleteEmployeeById(
            @PathVariable final Integer id);

    @GetMapping(value = "/api/roles/{roleName}")
    ResponseEntity<RoleResponse> getRoleByName(
            @PathVariable final String roleName);
}
