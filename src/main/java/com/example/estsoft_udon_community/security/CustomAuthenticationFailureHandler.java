package com.example.estsoft_udon_community.security;


import com.example.estsoft_udon_community.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final UsersRepository usersRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // 입력한 사용자 아이디
        String loginId = request.getParameter("loginId");

        // 기본 오류 메시지
        String errorMessage = "로그인 실패";

        System.out.println(loginId);

        // 사용자 존재 여부 확인
        boolean userExists = usersRepository.findByLoginId(loginId) != null;

        // 실패 이유에 따라 메시지를 설정
        if (exception instanceof BadCredentialsException) {
            if (!userExists) {
                errorMessage = "잘못된 로그인 ID입니다.";
            } else {
                errorMessage = "비밀번호가 일치하지 않습니다.";
            }
        } else if (exception instanceof DisabledException) {
            errorMessage = "계정이 비활성화되어 있습니다.";
        } else if (exception instanceof LockedException) {
            errorMessage = "계정이 잠겨 있습니다.";
        } else if (exception instanceof AccountExpiredException) {
            errorMessage = "계정의 유효 기간이 만료되었습니다.";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = "비밀번호의 유효 기간이 만료되었습니다.";
        }

        // 모델에 오류 메시지를 추가
        request.getSession().setAttribute("errorMessage", errorMessage);

        // 로그인 페이지로 리다이렉트
        response.sendRedirect("/login?error=true");
    }
}