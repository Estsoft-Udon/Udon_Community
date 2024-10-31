package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.entity.dto.UsersRequest;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    // 회원가입 -> insert user
    public Users registerUser(UsersRequest request) {
        return usersRepository.save(request.convert());
    }
    // Login ->

}
