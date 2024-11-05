package com.example.estsoft_udon_community.config;

import com.example.estsoft_udon_community.security.UsersDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public WebSecurityCustomizer ignore() {
        return WebSecurity -> WebSecurity.ignoring()
                .requestMatchers("/css/**", "/js/**", "/img/**"); // 정적 리소스 허용
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        custom -> custom.requestMatchers("/api/**").permitAll()
                                .requestMatchers("/login", "/signup", "/find_id", "/find_pw").permitAll()
//                                .anyRequest().hasAnyRole("UDON", "UDON_FRIEND", "UDON_SHERIFF", "UDON_MASTER", "UDON_ADMIN")
                                .anyRequest().permitAll()
                )
                .formLogin(custom -> custom.loginPage("/login"))

                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}