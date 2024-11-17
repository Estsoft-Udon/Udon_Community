package com.example.estsoft_udon_community.email;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class AuthCodeService {

    private final StringRedisTemplate redisTemplate;

    public AuthCodeService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveAuthCode(String email, String authCode) {
        // 인증번호를 Redis에 저장, 유효기간 5분 설정
        redisTemplate.opsForValue().set(email, authCode, 5, TimeUnit.MINUTES);
    }

    public boolean validateAuthCode(String email, String authCode) {
        String storedCode = redisTemplate.opsForValue().get(email);
        return authCode.equals(storedCode);
    }

    public void updateAuthStatus(String email) {
        redisTemplate.opsForValue().set("auth:email:" + email, "verified", Duration.ofHours(1)); // 1시간 동안 유지
    }

    public boolean isEmailVerified(String email) {
        String status = redisTemplate.opsForValue().get("auth:email:" + email);
        return "verified".equals(status);
    }
}