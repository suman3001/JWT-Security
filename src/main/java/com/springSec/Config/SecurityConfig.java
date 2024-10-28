package com.springSec.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {
    private JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);
       // http.authorizeHttpRequests().anyRequest().permitAll();
        http.authorizeHttpRequests()
                .requestMatchers("/secapi/v1/users/login","/secapi/v1/users/signup" , "/secapi/v1/users/signup-propertyowner")
                .permitAll()
                .requestMatchers("/secapi/v1/country/addCountry").hasAnyRole("OWNER","ADMIN")
                .anyRequest().authenticated();
     return http.build();
    }

}

