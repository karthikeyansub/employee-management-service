package com.bank.employee.management.service.client;

import feign.auth.BasicAuthRequestInterceptor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


public class EdsClientConfig {

//    @Value("${eds.username")
//    private String userName;
//
//    @Value("${eds.username")
//    private String password;

    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("username", "password");
    }
}
