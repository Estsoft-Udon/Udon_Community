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
    private final UsersDetailService usersDetailService;

    @Bean
    public WebSecurityCustomizer ignore() {
        return WebSecurity -> WebSecurity.ignoring()
                .requestMatchers("/css/**", "/js/**", "/img/**"); // 정적 리소스 허용
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(custom -> custom
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/login", "/signup", "/find_id", "/find_pw", "/success").permitAll()
                        .anyRequest().hasAnyRole("UDON", "UDON_FRIEND", "UDON_SHERIFF", "UDON_MASTER", "UDON_ADMIN")
                )
                .formLogin(custom -> custom
                        .loginPage("/login")
                        .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉션할 페이지
                        .permitAll() // 모든 사용자에게 로그인 페이지 접근 허용
                )
                .logout(custom -> custom
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)) // 항상 세션 생성
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}