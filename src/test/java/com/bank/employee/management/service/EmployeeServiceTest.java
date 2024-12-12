package com.bank.employee.management.service;

import com.bank.employee.management.domain.EdsEmployeeRequest;
import com.bank.employee.management.domain.EmployeeRequest;
import com.bank.employee.management.domain.EmployeeResponse;
import com.bank.employee.management.mapper.EmployeeMapper;
import com.bank.employee.management.service.client.EdsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableRetry
class EmployeeServiceTest {

    private EmployeeService classUnderTest;

    @Mock
    private EdsClient mockEdsClient;

    @Mock
    private EmployeeMapper mockEmployeeMapper;

    @BeforeEach
    void setUp() {
        classUnderTest = new EmployeeService(mockEdsClient, mockEmployeeMapper);
    }

    @Nested
    @DisplayName("Happy FLow")
    class HappyFlow {

        @Test
        void testCreateEmployee_ShouldReturnSavedEmployee() {
            when(mockEmployeeMapper.mapEmployeeRequestToEdsEmployeeRequest(any(EmployeeRequest.class)))
                    .thenReturn(getEdsEmployeeRequest());
            when(mockEdsClient.createEmployee(any(EdsEmployeeRequest.class)))
                    .thenReturn(getEmployeeResponse());

            EmployeeResponse response = classUnderTest.createEmployee(
                    getEmployeeRequest());

            assertNotNull(response);
            assertEquals("John Wick", response.name());
            assertEquals(1, response.roleId());
        }

//        @Test
//        void testCreateEmployee_ShouldRetry3Times_And_ReturnResponseOnThirdTime() {
//            FeignException feignException = mock(RetryableException.class);
//            when(mockEmployeeMapper.mapEmployeeRequestToEdsEmployeeRequest(any(EmployeeRequest.class)))
//                    .thenReturn(getEdsEmployeeRequest());
//            when(mockEdsClient.createEmployee(any(EdsEmployeeRequest.class)))
//                    .thenThrow(feignException)
//                    .thenThrow(feignException)
//                    .thenReturn(getEmployeeResponse());
//
//            EmployeeResponse response = classUnderTest.createEmployee(
//                    getEmployeeRequest());
//
//            assertNotNull(response);
//            assertEquals("John Wick", response.name());
//            assertEquals(1, response.roleId());
//            verify(mockEdsClient, times(3)).createEmployee(any(EdsEmployeeRequest.class));
//        }


        @Test
        void testGetEmployeeById_ShouldReturn200ResponseWithEmployee() {
            when(mockEdsClient.getEmployeeById(anyInt()))
                    .thenReturn(getEmployeeResponse());

            EmployeeResponse response = classUnderTest.getEmployeeById(1);

            assertNotNull(response);
            assertEquals("John Wick", response.name());
            assertEquals(1, response.roleId());
        }

        @Test
        void testUpdateEmployee_ShouldReturnUpdatedEmployee() {
            when(mockEmployeeMapper.mapEmployeeRequestToEdsEmployeeRequest(any(EmployeeRequest.class)))
                    .thenReturn(getEdsEmployeeRequest());
            when(mockEdsClient.updateEmployee(any(EdsEmployeeRequest.class), anyInt()))
                    .thenReturn(getEmployeeResponse());

            EmployeeResponse response = classUnderTest.updateEmployee(1, getEmployeeRequest());

            assertNotNull(response);
            assertEquals("John Wick", response.name());
            assertEquals(1, response.roleId());
        }

        @Test
        void testDeleteEmployeeById_ShouldDeleteEmployeeAndReturnMessage() {
            when(mockEdsClient.deleteEmployeeById(anyInt()))
                    .thenReturn(ResponseEntity.ok("Employee delete successfully"));

            String response = classUnderTest.deleteEmployeeById(1);

            assertNotNull(response);
            assertEquals("Employee delete successfully", response);
        }
    }


    private EdsEmployeeRequest getEdsEmployeeRequest() {
        return new EdsEmployeeRequest("John Wick", 1);
    }

    private ResponseEntity<EmployeeResponse> getEmployeeResponse() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new EmployeeResponse(1, "John Wick", 1));
    }

    private EmployeeRequest getEmployeeRequest() {
        return EmployeeRequest.builder()
                .firstname("John")
                .surname("Wick")
                .roleId(1).build();
    }
}