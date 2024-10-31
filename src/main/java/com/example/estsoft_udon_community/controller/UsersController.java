package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.entity.dto.UsersRequest;
import com.example.estsoft_udon_community.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UsersController {

    private final UsersService usersService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(UsersRequest user) {
        usersService.registerUser(user);
        return ResponseEntity.ok(user.convert());
    }
}
