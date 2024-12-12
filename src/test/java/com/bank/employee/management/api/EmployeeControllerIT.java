package com.bank.employee.management.api;

import com.bank.employee.management.AbstractWireMockTest;
import com.bank.employee.management.domain.EdsEmployeeRequest;
import com.bank.employee.management.domain.EmployeeRequest;
import com.bank.employee.management.domain.EmployeeResponse;
import com.bank.employee.management.domain.RoleResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import util.EmployeeTestUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmployeeControllerIT extends AbstractWireMockTest {


    private static final String EMPLOYEE_API_URI = "/employees";

        @Test
        @DisplayName("POST:/employees - API will return the 200 response with employee response")
        void testCreateEmployee_ShouldReturn200ResponseWithEmployee() throws Exception {
            stubFor(
                    WireMock.get(urlMatching("/api/roles/.*"))
                            .willReturn(aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .withHeader(HttpHeaders.AUTHORIZATION, "Bearer test")
                                    .withBody(covertToJsonString(new RoleResponse(1, "ADMIN")).getBytes()))
            );
            stubFor(
                    WireMock.post(urlEqualTo("/api/employees"))
                            .willReturn(aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                    .withHeader(HttpHeaders.AUTHORIZATION, "Bearer test")
                                    .withBody(covertToJsonString(new EdsEmployeeRequest("Tom Hanks", 1)).getBytes()))
            );

            EmployeeRequest request = getEmployeeRequest("Tom", "Hanks");
            MvcResult result = mockMvc.perform(post(EMPLOYEE_API_URI)
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForAdmin())
                            .header("role", "ADMIN")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(getEmployeeRequest("Tom", "Hanks"))))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            EmployeeResponse response = objectMapper.readValue(responseJson, EmployeeResponse.class);
            assertEquals("Tom Hanks", response.name());
            assertEquals(1, response.roleId());
        }


    private EmployeeRequest getEmployeeRequest(final String firstName, final String surName) {
        return EmployeeRequest.builder()
                .firstname(firstName)
                .surname(surName)
                .build();
    }

    private String covertToJsonString(final Object request) throws JsonProcessingException {
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }
}