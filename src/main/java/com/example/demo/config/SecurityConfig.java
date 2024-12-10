package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http // Disable CSRF for simplicity (not recommended for production)
                .csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/auth/register","/auth/login", "/auth/testcalltoOrder/**","/auth/test-resilience","/swagger-ui/index.html","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                        // Require authentication for all other requests
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}