package com.bank.employee.management.config;

import com.bank.employee.management.domain.security.UserAccountListProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(UserAccountListProperties.class)
@AllArgsConstructor
public class WebSecurityConfig {

    private static final String ADMIN = "ADMIN";

    private static final String USER = "USER";

    private final UserAccountListProperties userAccounts;

    @Bean
    public SecurityFilterChain securityFilterChainForTest(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(requests -> requests
                    .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                    .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
                    .requestMatchers(antMatcher(HttpMethod.GET, "/actuator/**")).permitAll()
                    .requestMatchers(antMatcher(HttpMethod.OPTIONS, "/**")).permitAll()
                    .requestMatchers(antMatcher(HttpMethod.GET, "/employees/**")).hasRole(USER)
                    .requestMatchers(antMatcher(HttpMethod.POST, "/employees")).hasRole(ADMIN)
                    .requestMatchers(antMatcher(HttpMethod.PUT, "/employees/**")).hasRole(USER)
                    .requestMatchers(antMatcher(HttpMethod.DELETE, "/employees/**")).hasRole(ADMIN)
                    .anyRequest().denyAll())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(httpSecurityHttpBasicConfigurer ->
                        httpSecurityHttpBasicConfigurer.realmName("Basic"))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        userAccounts.getConfig().forEach(userAccount ->
                manager.createUser(User.withUsername(userAccount.getUsername())
                    .password(bCryptPasswordEncoder.encode(userAccount.getPassword()))
                    .roles(userAccount.getRole()).build()));
        return manager;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(5);
    }
}