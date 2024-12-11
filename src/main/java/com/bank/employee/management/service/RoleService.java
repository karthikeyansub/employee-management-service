package com.bank.employee.management.service;

import com.bank.employee.management.service.client.EdsClient;
import com.bank.employee.management.domain.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class RoleService {

    private final EdsClient edsClient;

    public Integer getRoleIdByName(final String roleName) {
        ResponseEntity<RoleResponse> response = edsClient.getRoleByName(roleName);
        log.debug("Role response: {}", response.getBody());
        //TODO:
        return response.getBody().id();
    }
}
