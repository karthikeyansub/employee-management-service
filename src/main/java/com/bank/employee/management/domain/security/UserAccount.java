package com.bank.employee.management.domain.security;

import lombok.Data;

@Data
public class UserAccount {

    private String username;
    private String password;
    private String role;
}
