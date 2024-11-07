package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.request.UpdateArticleRequest;
import com.example.estsoft_udon_community.dto.request.UsersRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.UsersResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import com.example.estsoft_udon_community.service.admin.AdminArticleService;
import com.example.estsoft_udon_community.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
public class AdminBoardController {
    private final AdminArticleService adminArticleService;
    private final ArticlesRepository articlesRepository;

    // 관리자 게시글 조회
    @GetMapping("/")
    public List<ArticleResponse> getAdminArticles() {
        // 모든 게시글을 가져오기
        List<Articles> articles = articlesRepository.findAll();

        // ArticleResponse로 변환하여 반환
        return articles.stream()
                .map(ArticleResponse::new) // ArticleResponse 생성자 호출
                .collect(Collectors.toList());
    }
//    public ResponseEntity<List<ArticleResponse>> getAdminArticles() {
//        List<ArticleResponse> adminArticles = adminArticleService.getAdminArticles();
//        return ResponseEntity.ok(adminArticles);
//    }

//    // 관리자 특정 게시글 조회
//    @GetMapping("/{articleId}")
//    public ResponseEntity<ArticleResponse> getAdminByArticleId(@PathVariable Long articleId) {
//        Optional<ArticleResponse> article = adminService.getAdminByArticleId(articleId);
//        return article.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    // 관리자 게시글 수정
//    @PutMapping("/{articleId}")
//    public ResponseEntity<ArticleResponse> updateAdminArticle(@PathVariable Long articleId, @RequestBody UpdateArticleRequest request) {
//        Articles updatedArticle = adminService.updateArticle(articleId, request);
//        return ResponseEntity.ok(new ArticleResponse(updatedArticle));
//    }
}