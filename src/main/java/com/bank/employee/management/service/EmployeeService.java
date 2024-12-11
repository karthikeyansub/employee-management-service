package com.bank.employee.management.service;

import com.bank.employee.management.domain.EmployeeRequest;
import com.bank.employee.management.mapper.EmployeeMapper;
import com.bank.employee.management.service.client.EdsClient;
import com.bank.employee.management.domain.EdsEmployeeRequest;
import com.bank.employee.management.domain.EmployeeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    public EmployeeResponse createEmployee(final EmployeeRequest employeeRequest) {
        EdsEmployeeRequest edsEmployeeRequest = employeeMapper.mapEmployeeRequestToEdsEmployeeRequest(employeeRequest);
        log.debug("Create employee request - {}", edsEmployeeRequest);

        //TODO: Handle exception
        ResponseEntity<EmployeeResponse> response = edsClient.createEmployee(edsEmployeeRequest);
        log.debug("Created employee response - {}", response.getBody());

        return response.getBody();
    }

    public EmployeeResponse getEmployeeById(final int employeeId) {
        //TODO: Handle exception
        ResponseEntity<EmployeeResponse> response = edsClient.getEmployeeById(employeeId);
        log.debug("Retrieved employee response - {}", response.getBody());

        return response.getBody();
    }


    public EmployeeResponse updateEmployee(final int employeeId, final EmployeeRequest employeeRequest) {
        EdsEmployeeRequest edsEmployeeRequest = employeeMapper.mapEmployeeRequestToEdsEmployeeRequest(employeeRequest);
        log.debug("Update employee request - {}", edsEmployeeRequest);

        //TODO: Handle exception
        ResponseEntity<EmployeeResponse> response = edsClient.updateEmployee(edsEmployeeRequest, employeeId);
        log.debug("Updated employee response - {}", response.getBody());

        return response.getBody();
    }

    public String deleteEmployeeById(final Integer employeeId) {
        //TODO: Handle exception
        ResponseEntity<String> response = edsClient.deleteEmployeeById(employeeId);
        log.debug("Deleted employee response - {}", response.getBody());

        return response.getBody();
    }
}
