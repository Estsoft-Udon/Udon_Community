package com.example.estsoft_udon_community.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Bean
    public WebSecurityCustomizer ignore() {
        return WebSecurity -> WebSecurity.ignoring()
                .requestMatchers("/css/**", "js/**", "/img/**", "/error"); // 정적 리소스 허용
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        custom -> custom.requestMatchers("/api/**").permitAll()
                                .requestMatchers("/", "/login", "/signup", "/find_id", "/find_pw","/getLowerLocations", "/success").permitAll()
                                .requestMatchers("/admin/**").hasAuthority("ROLE_UDON_ADMIN")
                                .anyRequest().hasAnyRole("UDON", "UDON_FRIEND", "UDON_SHERIFF", "UDON_MASTER", "UDON_ADMIN")
                )
                .formLogin(custom -> {
                    custom.loginPage("/login");
                })

                // 권한이 없는 사용자 접근 시 에러 페이지 설정
                .exceptionHandling(custom -> {
                    custom.accessDeniedPage("/access-denied"); // 권한 없는 사용자가 접근할 경우 리다이렉트할 페이지
                })

                // 로그아웃 추가
                .logout(custom -> {
                    custom.logoutUrl("/logout")
                            .logoutSuccessUrl("/");
                })

                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}