package com.bank.employee.management.service;

import com.bank.employee.management.domain.RoleResponse;
import com.bank.employee.management.service.client.EdsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RoleServiceTest {

    private RoleService classUnderTest;

    @Mock
    private EdsClient mockEdsClient;

    @BeforeEach
    void setUp() {
        classUnderTest = new RoleService(mockEdsClient);
    }

    @Test
    void testGetRoleIdByName_ShouldReturnRoleId() {
        when(mockEdsClient.getRoleByName(anyString())).thenReturn(ResponseEntity.ok(new RoleResponse(1, "ADMIN")));

        Integer roleId = classUnderTest.getRoleIdByName("ADMIN");

        assertEquals(1, roleId);
    }

}