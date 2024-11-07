package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.request.UpdateArticleRequest;
import com.example.estsoft_udon_community.dto.request.UsersRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.UsersResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Users updateUser = adminService.updateUserGrade(userId, grade);
        return ResponseEntity.ok(new UsersResponse(updateUser));
    }

    // 관리자 게시글 조회
//    @GetMapping("/articles")
//    public ResponseEntity<List<ArticleResponse>> getAdminArticles() {
//        List<ArticleResponse> adminArticles = adminService.getAdminArticles();
//        return ResponseEntity.ok(adminArticles);
//    }

    // 관리자 특정 게시글 조회
    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ArticleResponse> getAdminByArticleId(@PathVariable Long articleId) {
        Optional<ArticleResponse> article = adminService.getAdminByArticleId(articleId);
        return article.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 관리자 게시글 수정
    @PutMapping("/articles/{articleId}")
    public ResponseEntity<ArticleResponse> updateAdminArticle(@PathVariable Long articleId,
                                                              @RequestBody UpdateArticleRequest request) {
        Articles updatedArticle = adminService.updateArticle(articleId, request);
        return ResponseEntity.ok(new ArticleResponse(updatedArticle));
    }
}