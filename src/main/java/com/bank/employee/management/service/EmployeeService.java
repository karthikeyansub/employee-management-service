package com.bank.employee.management.service;

import com.bank.employee.management.domain.ApiSuccessResponse;
import com.bank.employee.management.domain.EmployeeRequest;
import com.bank.employee.management.mapper.EmployeeMapper;
import com.bank.employee.management.service.client.EdsClient;
import com.bank.employee.management.domain.EdsEmployeeRequest;
import com.bank.employee.management.domain.EmployeeResponse;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeMapper employeeMapper;

    private final EdsClient edsClient;

    public EmployeeService(final EdsClient edsClient, final EmployeeMapper employeeMapper) {
        this.edsClient = edsClient;
        this.employeeMapper = employeeMapper;
    }

    @Retryable(retryFor = RetryableException.class, backoff = @Backoff(delay = 100))
    public EmployeeResponse createEmployee(final EmployeeRequest employeeRequest) {
        EdsEmployeeRequest edsEmployeeRequest = employeeMapper.mapEmployeeRequestToEdsEmployeeRequest(employeeRequest);
        log.debug("Create employee request - {}", edsEmployeeRequest);

        ResponseEntity<EmployeeResponse> response = edsClient.createEmployee(edsEmployeeRequest);

        return response.getBody();
    }

    @Retryable(retryFor = RetryableException.class, backoff = @Backoff(delay = 100))
    public EmployeeResponse getEmployeeById(final int employeeId) {
        ResponseEntity<EmployeeResponse> response = edsClient.getEmployeeById(employeeId);
        log.debug("Retrieved employee response - {}", response.getBody());

        return response.getBody();
    }

    @Retryable(retryFor = RetryableException.class, backoff = @Backoff(delay = 100))
    public EmployeeResponse updateEmployee(final int employeeId, final EmployeeRequest employeeRequest) {
        EdsEmployeeRequest edsEmployeeRequest = employeeMapper.mapEmployeeRequestToEdsEmployeeRequest(employeeRequest);
        log.debug("Update employee request - {}", edsEmployeeRequest);

        ResponseEntity<EmployeeResponse> response = edsClient.updateEmployee(employeeId, edsEmployeeRequest);
        log.debug("Updated employee response - {}", response.getBody());

        return response.getBody();
    }

    @Retryable(retryFor = RetryableException.class, backoff = @Backoff(delay = 100))
    public ApiSuccessResponse deleteEmployeeById(final Integer employeeId) {
        ResponseEntity<ApiSuccessResponse> response = edsClient.deleteEmployeeById(employeeId);
        log.debug("Deleted employee response - {}", response.getBody());

        return response.getBody();
    }
}
