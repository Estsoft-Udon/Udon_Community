package com.example.estsoft_udon_community.util;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    // 현재 로그인된 사용자의 ID를 가져오는 메서드
    public static Long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUser().getId(); // CustomUserDetails에서 사용자 ID를 반환
        }
        throw new IllegalStateException("현재 로그인된 사용자가 없습니다.");
    }

    public static Users getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUser(); // CustomUserDetails에서 사용자 ID를 반환
        }
        throw new IllegalStateException("현재 로그인된 사용자가 없습니다.");
    }
}