package com.bank.employee.management.service;

import com.bank.employee.management.service.client.EdsClient;
import com.bank.employee.management.domain.RoleResponse;
import feign.RetryableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class RoleService {

    private final EdsClient edsClient;

    @Retryable(retryFor = RetryableException.class, backoff = @Backoff(delay = 100))
    public Integer getRoleIdByName(final String roleName) {
        ResponseEntity<RoleResponse> response = edsClient.getRoleByName(roleName);
        log.debug("Role response: {}", response.getBody());
        return null != response.getBody() ? response.getBody().id() : null;
    }
}
