package com.example.estsoft_udon_community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public WebSecurityCustomizer ignore() {
        return WebSecurity -> WebSecurity.ignoring()
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .authorizeRequests()
//                .requestMatchers("/api/register", "/api/login").permitAll() // Allow access to register and login endpoints
//                .anyRequest().authenticated() // Secure all other endpoints
//                .and()
//                .csrf().disable(); // Disable CSRF for testing (remove this in production)

        return http.authorizeHttpRequests(
                        custom -> custom.requestMatchers("/api/**").permitAll()
                                .anyRequest().permitAll()
                ).csrf(custom -> custom.disable())
                .build();
    }
}
