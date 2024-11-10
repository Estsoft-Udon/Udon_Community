package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.ToggleVisibilityRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import com.example.estsoft_udon_community.service.admin.AdminArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
public class AdminBoardController {
    private final AdminArticleService adminArticleService;
    private final ArticlesRepository articlesRepository;

    // 관리자 게시글 조회
    @GetMapping("/")
    public ResponseEntity<Page<ArticleResponse>> getAdminArticles(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        // 모든 게시글을 가져오기
        Page<ArticleResponse> articles = adminArticleService.getAdminArticles(page, size);
        return ResponseEntity.ok(articles);
    }

    // 관리자 특정 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getEventById(@PathVariable Long id) {
        ArticleResponse articleResponse = adminArticleService.getArticleById(id);
        return ResponseEntity.ok(articleResponse);
    }

//    // 관리자 게시글 수정
    @PutMapping("/{id}/toggle-visibility")
    public ResponseEntity<ArticleResponse> toggleArticleVisibility(@PathVariable Long id, @RequestBody ToggleVisibilityRequest request) {
        // 가시성 토글
        Articles updatedArticle = adminArticleService.toggleArticleVisibility(id, request.getVisibility());

        // 응답으로 변경된 게시글 정보 반환
        return ResponseEntity.ok(new ArticleResponse(updatedArticle));
    }

}