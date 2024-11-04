package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UsersRepository usersRepository;

    // 관리자 로그인
    public Users adminLogin(String loginId, String password) {
        Users adminUser = usersRepository.findByLoginId(loginId);
        if (adminUser == null || !adminUser.getPassword().equals(password) || !adminUser.getGrade().equals(Grade.UDON_ADMIN)) {
            throw new IllegalArgumentException("관리자가 아님");
        }
        return adminUser;
    }
}