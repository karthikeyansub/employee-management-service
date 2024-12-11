package com.bank.employee.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EmployeeManagementServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(EmployeeManagementServiceApplication.class, args);
	}

}
