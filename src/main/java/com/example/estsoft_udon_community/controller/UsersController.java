package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.entity.request.UsersRequest;
import com.example.estsoft_udon_community.service.UsersService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Users> registerUser(@RequestBody UsersRequest user) {
        usersService.registerUser(user);
        return ResponseEntity.ok(user.convert());
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Users> loginUser(String loginId, String password) {
        Users users = usersService.loginUser(loginId, password);
        return ResponseEntity.ok(users);
    }

    // 유저 정보 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<Users> findUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(usersService.findUserById(userId));
    }

    // 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<Users> updateUser(@PathVariable Long userId, @RequestBody UsersRequest user) {
        return ResponseEntity.ok(usersService.updateUser(userId, user));
    }

    // 삭제
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Users> deleteUser(@PathVariable Long userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/searchId")
    public ResponseEntity<String> findUserBySearchId(@RequestBody UsersRequest user) {
        Users foundUser = usersService.searchId(user.getName(), user.getEmail());

        // 예외 상황은 아직 생각 안함

        return ResponseEntity.ok(foundUser.getLoginId());
    }

    @PostMapping("/searchPassword")
    public ResponseEntity<String> findUserByPassword(@RequestBody UsersRequest user) {
        Users foundUser = usersService.searchPassword(user.getLoginId(), user.getPasswordHint(),
                user.getPasswordAnswer());

        // 예외 상황은 아직 생각 안함

        return ResponseEntity.ok(foundUser.getPassword());
    }

}