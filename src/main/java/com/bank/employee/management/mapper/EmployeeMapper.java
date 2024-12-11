package com.bank.employee.management.mapper;

import com.bank.employee.management.domain.EmployeeRequest;
import com.bank.employee.management.domain.EdsEmployeeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "name", expression = """
                java(employeeRequest.getFirstname() + " " + employeeRequest.getSurname())
            """)
    EdsEmployeeRequest mapEmployeeRequestToEdsEmployeeRequest(final EmployeeRequest employeeRequest);

}
