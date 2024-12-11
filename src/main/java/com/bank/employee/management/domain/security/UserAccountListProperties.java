package com.bank.employee.management.domain.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("basicauth")
@Data
public class UserAccountListProperties {

    private List<UserAccount> config;
}
