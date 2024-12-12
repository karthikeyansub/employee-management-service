package com.bank.employee.management.api;

import com.bank.employee.management.domain.EdsEmployeeRequest;
import com.bank.employee.management.domain.EmployeeRequest;
import com.bank.employee.management.domain.EmployeeResponse;
import com.bank.employee.management.domain.RoleResponse;
import com.bank.employee.management.service.client.EdsClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import util.EmployeeTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIT {

    private EmployeeController classUnderTest;

    @Mock
    private EdsClient mockEdsClient;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String EMPLOYEE_API_URI = "/employees";

    @Nested
    @DisplayName("Happy Flow")
    class HappyFloww {

        @Test
        @DisplayName("POST:/employees - API will return the 200 response with employee response")
        void testCreateEmployee_ShouldReturn200ResponseWithEmployee() throws Exception {
            when(mockEdsClient.createEmployee(any(EdsEmployeeRequest.class)))
                    .thenReturn(getEmployeeResponse("Tom Hanks", 1));
            when(mockEdsClient.getRoleByName("ADMIN")).thenReturn(getRoleResponse(1, "ADMIN"));

            EmployeeRequest request = getEmployeeRequest("Tom", "Hanks");
            MvcResult result = mockMvc.perform(post(EMPLOYEE_API_URI)
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForAdmin())
                            .header("role", "ADMIN")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(request)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            EmployeeResponse response = objectMapper.readValue(responseJson, EmployeeResponse.class);
            assertEquals("Tom Hanks", response.name());
            assertEquals(2, response.roleId());
        }
    }

    private ResponseEntity<EmployeeResponse> getEmployeeResponse(
            final String name, final Integer roleId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new EmployeeResponse(1, name, roleId));
    }

    private EmployeeRequest getEmployeeRequest(final String firstName, final String surName) {
        return EmployeeRequest.builder()
                .firstname(firstName)
                .surname(surName)
                .build();
    }

    private ResponseEntity<RoleResponse> getRoleResponse(final Integer roleId, final String roleName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RoleResponse(roleId, roleName));
    }

    private String covertToJsonString(final EmployeeRequest request) throws JsonProcessingException {
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }
}