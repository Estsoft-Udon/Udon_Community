package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.response.UsersResponse;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.dto.request.UsersRequest;
import com.example.estsoft_udon_community.service.UsersService;
import java.util.List;
import java.util.Map;

import com.example.estsoft_udon_community.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UsersController {

    private final UsersService usersService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UsersResponse> registerUser(@RequestBody UsersRequest user) {
        return ResponseEntity.ok(new UsersResponse(usersService.registerUser(user)));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UsersResponse>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers().stream().map(UsersResponse::new).toList());
    }

    // 유저 정보 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<UsersResponse> findUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(new UsersResponse(usersService.findUserById(userId)));
    }

    // 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<UsersResponse> updateUser(@PathVariable Long userId, @RequestBody UsersRequest usersRequest) {
        return ResponseEntity.ok(new UsersResponse(usersService.updateUser(userId, usersRequest)));
    }

    // 삭제
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/searchId")
    public ResponseEntity<String> findUserBySearchId(@RequestBody UsersRequest user) {
        Users foundUser = usersService.searchId(user.getName(), user.getEmail());
        return ResponseEntity.ok(foundUser.getLoginId());
    }

    @PostMapping("/searchPassword")
    public ResponseEntity<String> findUserByPassword(@RequestBody UsersRequest user) {
        Users foundUser = usersService.searchPassword(user.getLoginId(), user.getPasswordHint(),
                user.getPasswordAnswer());
        return ResponseEntity.ok(foundUser.getPassword());
    }

    // 회원가입시 아이디 중복체크
    @PostMapping("/checkId")
    public ResponseEntity<Boolean> checkId(@RequestBody Map<String, String> requestBody) {
        String loginId = requestBody.get("loginId");
        boolean isDuplicate = usersService.isLoginIdDuplicate(loginId);
        return ResponseEntity.ok(isDuplicate);
    }

    // 회원가입시 닉네임 중복체크
    @PostMapping("/checkNickname")
    public ResponseEntity<Boolean> checkNickname(@RequestBody Map<String, String> requestBody) {
        String nickname = requestBody.get("nickname");
        boolean isDuplicate = usersService.isLoginCheckNickname(nickname);
        return ResponseEntity.ok(isDuplicate);
    }

    // 회원가입시 email 중복체크
    @PostMapping("/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        boolean isDuplicate = usersService.isLoginCheckEmail(email);
        return ResponseEntity.ok(isDuplicate);
    }

    // 회원탈퇴
    @PostMapping("/withdrawal")
    public ResponseEntity<Void> doWithdrawal(@RequestBody Map<String, String> request) {
        String password = request.get("password");

        Users user = usersService.findUserById(SecurityUtil.getLoggedInUser().getId());
        if(usersService.softDelete(user, password)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}