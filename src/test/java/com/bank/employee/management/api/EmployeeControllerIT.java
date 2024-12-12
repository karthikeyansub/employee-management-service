package com.bank.employee.management.api;

import com.bank.employee.management.AbstractWireMockTest;
import com.bank.employee.management.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import util.EmployeeTestUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmployeeControllerIT extends AbstractWireMockTest {

    private static final String EMPLOYEE_API_URI = "/employees";

    @Nested
    @DisplayName("Happy Flow")
    class HappyFlow {
        @Test
        @DisplayName("POST:/employees - API will return the 200 response with employee response")
        void testCreateEmployee_ShouldReturn200ResponseWithEmployee() throws Exception {
            stubForRoles();
            stubFor(
                    WireMock.post(urlEqualTo("/api/employees"))
                            .willReturn(aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .withBody(covertToJsonString(new EmployeeResponse(1, "Tom Hanks", 1)).getBytes())));

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
            assertEquals(1, response.id());
            assertEquals("Tom Hanks", response.name());
            assertEquals(1, response.roleId());
        }

        @Test
        @DisplayName("GET:/employees/{id} - API will return the 200 response with employee response")
        void testGetEmployeeById_ShouldReturn200ResponseWithEmployee() throws Exception {
            stubFor(
                    WireMock.get(urlEqualTo("/api/employees/1"))
                            .willReturn(aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .withBody(covertToJsonString(new EmployeeResponse(1,"Tom Hanks", 1)).getBytes())));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(EMPLOYEE_API_URI + "/1")
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForUser())
                            .header("role", "USER"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            EmployeeResponse response = objectMapper.readValue(responseJson, EmployeeResponse.class);
            assertEquals(1, response.roleId());
            assertEquals("Tom Hanks", response.name());
            assertEquals(1, response.roleId());
        }

        @Test
        @DisplayName("PUT:/employees/{id} - API will return the 200 response with updated employee response")
        void testUpdateEmployee_ShouldReturn200ResponseWithEmployee() throws Exception {
            stubForRoles();
            stubFor(WireMock.put(urlEqualTo("/api/employees/1"))
                            .willReturn(aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .withBody(covertToJsonString(new EmployeeResponse(1, "Tom Hanks", 1)).getBytes())));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(EMPLOYEE_API_URI + "/1")
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForUser())
                            .header("role", "ADMIN")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(getEmployeeRequest("Tom", "Hanks"))))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            EmployeeResponse response = objectMapper.readValue(responseJson, EmployeeResponse.class);
            assertEquals(1, response.id());
            assertEquals("Tom Hanks", response.name());
            assertEquals(1, response.roleId());
        }

        @Test
        @DisplayName("DELETE:/employees/[id} - API will return the 200 response with success message")
        void testDeleteEmployee_ShouldReturn200ResponseWithSuccessMessage() throws Exception {
            stubFor(WireMock.delete(urlEqualTo("/api/employees/1"))
                            .willReturn(aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .withBody(covertToJsonString(new ApiSuccessResponse("Employee deleted successfully")))));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(EMPLOYEE_API_URI + "/1")
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForAdmin())
                            .header("role", "ADMIN")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(getEmployeeRequest("Tom", "Hanks"))))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ApiSuccessResponse response = objectMapper.readValue(responseJson, ApiSuccessResponse.class);
            assertEquals("Employee deleted successfully", response.message());
        }
    }

    @Nested
    @DisplayName("Error Flow")
    class ErrorFlow {

        @Autowired
        private EmployeeController classUnderTest;

        @Test
        @DisplayName("POST:/employees - API will return the 403 forbidden if user role perform create operation")
        void testCreateEmployee_ShouldReturnForbidden_IfRoleBasedAccessNotMatch() throws Exception {
            mockMvc.perform(post(EMPLOYEE_API_URI)
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForUser())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(getEmployeeRequest("Tom", "Hanks"))))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST:/employees - API will return the 400 bad request if role is missing")
        void testCreateEmployee_ShouldReturnBadRequest_IfRoleIsMissing() throws Exception {
            MvcResult result = mockMvc.perform(post(EMPLOYEE_API_URI)
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForAdmin())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(getEmployeeRequest("Tom", "Hanks"))))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ApiErrorResponse response = objectMapper.readValue(responseJson, ApiErrorResponse.class);
            assertEquals(HttpStatus.BAD_REQUEST.name(), response.errorTitle());
            assertEquals("Required request header 'role' for method parameter type String is not present", response.errorMessage());
        }

        @Test
        @DisplayName("POST:/employees - API will return the 400 bad request if role length is not between 3 to 50")
        void testCreateEmployee_ShouldReturnBadRequest_IfRoleLengthNotMatching() throws Exception {
            MvcResult result = mockMvc.perform(post(EMPLOYEE_API_URI)
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForAdmin())
                            .header("role", "UR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(getEmployeeRequest("Tom", "Hanks"))))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ApiErrorResponse response = objectMapper.readValue(responseJson, ApiErrorResponse.class);
            assertEquals(HttpStatus.BAD_REQUEST.name(), response.errorTitle());
            assertEquals("Role length should be between 3 to 50 characters", response.errorMessage());
        }

        @Test
        @DisplayName("POST:/employees - API will return the 400 bad request if firstname and surname is null")
        void testCreateEmployee_ShouldReturnBadRequest_IfFirstnameAndSurnameIsNull() throws Exception {
            MvcResult result = mockMvc.perform(post(EMPLOYEE_API_URI)
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForAdmin())
                            .header("role", "USER")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(getEmployeeRequest(null, null))))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ApiErrorResponse response = objectMapper.readValue(responseJson, ApiErrorResponse.class);
            assertEquals(HttpStatus.BAD_REQUEST.name(), response.errorTitle());
            assertTrue(response.errorDetails().contains("The field 'first_name' should not be null"));
            assertTrue(response.errorDetails().contains("The field 'sur_name' should not be null"));
        }

        @Test
        @DisplayName("POST:/employees - API will return the 400 bad request if firstname and surname is blank")
        void testCreateEmployee_ShouldReturnBadRequest_IfFirstnameAndSurnameIsBlank() throws Exception {
            MvcResult result = mockMvc.perform(post(EMPLOYEE_API_URI)
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForAdmin())
                            .header("role", "USER")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(covertToJsonString(getEmployeeRequest("", ""))))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ApiErrorResponse response = objectMapper.readValue(responseJson, ApiErrorResponse.class);
            assertEquals(HttpStatus.BAD_REQUEST.name(), response.errorTitle());
            assertTrue(response.errorDetails().contains("The field 'first_name' should not be empty"));
            assertTrue(response.errorDetails().contains("The field 'sur_name' should not be empty"));
        }

        @Test
        @DisplayName("GET:/employees/{id} - API will return the 404 employee not found exception")
        void testGetEmployeeById_ShouldReturnNotFoundResponse() throws Exception {
            stubFor(
                    WireMock.get(urlEqualTo("/api/employees/1"))
                            .willReturn(aResponse()
                                    .withStatus(HttpStatus.NOT_FOUND.value())
                                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .withBody(covertToJsonString(new ApiErrorResponse("NOT_FOUND", "Employee not found", null)).getBytes())));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(EMPLOYEE_API_URI + "/1")
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForUser())
                            .header("role", "USER"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ApiErrorResponse response = objectMapper.readValue(responseJson, ApiErrorResponse.class);
            assertEquals("NOT_FOUND", response.errorTitle());
        }

        @Test
        @DisplayName("GET:/employees/{id} - API will return the 500 internal server error")
        void testGetEmployeeById_ShouldReturnInternalServerErrorResponse() throws Exception {
            stubFor(
                    WireMock.get(urlEqualTo("/api/employees/1"))
                            .willReturn(aResponse()
                                    .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .withBody(covertToJsonString(new ApiErrorResponse("INTERNAL_SERVER_ERROR", "Something went wrong", null)).getBytes())));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(EMPLOYEE_API_URI + "/1")
                            .header(HttpHeaders.AUTHORIZATION, EmployeeTestUtils.basicAuthHeaderForUser())
                            .header("role", "USER"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isInternalServerError())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            ApiErrorResponse response = objectMapper.readValue(responseJson, ApiErrorResponse.class);
            assertEquals("INTERNAL_SERVER_ERROR", response.errorTitle());
            assertEquals("Something went wrong", response.errorMessage());
        }
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

    private void stubForRoles() throws Exception {
        stubFor(
                WireMock.get(urlMatching("/api/roles/.*"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(covertToJsonString(new RoleResponse(1, "ADMIN")).getBytes())));

    }
}