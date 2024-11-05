package com.example.estsoft_udon_community.config;

import com.example.estsoft_udon_community.security.CustomAuthenticationFailureHandler;
import com.example.estsoft_udon_community.service.UsersDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
    private final CustomAuthenticationFailureHandler failureHandler;

    @Bean
    public WebSecurityCustomizer ignore() {
        return WebSecurity -> WebSecurity.ignoring()
                .requestMatchers("/css/**", "/js/**", "/images/**"); // 정적 리소스 허용
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        custom -> custom.requestMatchers("/api/**").permitAll()
                                .requestMatchers("/login", "/signup", "/find_id", "/find_pw").permitAll()
                                .anyRequest().permitAll()
//                                .anyRequest().hasAnyRole("UDON", "UDON_FRIEND", "UDON_SHERIFF", "UDON_MASTER", "UDON_ADMIN")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.authorizeHttpRequests(
//                        custom -> custom.requestMatchers("/api/**").permitAll()
//                                .requestMatchers("/login", "/signup", "/find_id", "/find_pw").permitAll()
////                                .requestMatchers("/mypage").permitAll()
////                                .anyRequest().permitAll()
//                                .anyRequest().hasAnyRole("UDON", "UDON_FRIEND", "UDON_SHERIFF", "UDON_MASTER", "UDON_ADMIN")
////                                .anyRequest().authenticated()            // 그 외에는 인증 필요
//                )
////                .formLogin(custom -> custom.loginPage("/login"))
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // 세션 생성 정책 설정
////                .logout(custom -> custom.logoutSuccessUrl("/login")       // 로그아웃 설정
////                        .invalidateHttpSession(true))
//                .csrf(AbstractHttpConfigurer::disable)
//                .build();
//    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


