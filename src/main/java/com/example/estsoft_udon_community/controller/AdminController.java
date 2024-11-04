package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.UsersRequest;
import com.example.estsoft_udon_community.dto.response.UsersResponse;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // 관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<UsersResponse> login(@RequestBody UsersRequest request) {
        Users admin = adminService.adminLogin(request.getLoginId(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsersResponse(admin));
    }

    // 관리자 회원 관리 리스트
    @GetMapping("/users")
    public ResponseEntity<List<UsersResponse>> getAllUsers() {
        List<UsersResponse> userList = adminService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    // 회원 등급 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<UsersResponse> updateUserGrade(@PathVariable Long userId, @RequestBody Grade grade) {
        UsersResponse updateUser = adminService.updateUserGrade(userId, grade);
        return ResponseEntity.ok(updateUser);
    }
}