package com.example.estsoft_udon_community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        return http.authorizeHttpRequests(
                        custom -> custom.requestMatchers("/api/**").permitAll()
                                .anyRequest().permitAll()
                ).csrf(custom -> custom.disable())
                .build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.authorizeHttpRequests(
//                        custom -> custom.requestMatchers("/login").permitAll()
//                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스 허용
//                                .anyRequest().authenticated())          // 그 외에는 인증 필요
//                .formLogin(custom -> custom.loginPage("/login")         // 폼 기반 로그인 설정
//                        .defaultSuccessUrl("/articles"))                // 인증 성공 시 여기로 리다이렉트
//                .csrf(AbstractHttpConfigurer::disable)
//                .build();
//    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
