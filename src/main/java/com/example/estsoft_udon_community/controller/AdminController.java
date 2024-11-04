package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.UsersRequest;
import com.example.estsoft_udon_community.dto.response.UsersResponse;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}