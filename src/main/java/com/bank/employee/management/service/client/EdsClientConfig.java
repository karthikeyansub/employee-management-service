package com.bank.employee.management.service.client;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class EdsClientConfig {

    @Value("${eds.username}")
    private String userName;

    @Value("${eds.password}")
    private String password;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(userName, password);
    }
}
