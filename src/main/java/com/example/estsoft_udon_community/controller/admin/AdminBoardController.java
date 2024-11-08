package com.example.estsoft_udon_community.controller.admin;

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

        // ArticleResponse로 변환하여 반환
//        return articles.stream()
//                .map(ArticleResponse::new) // ArticleResponse 생성자 호출
//                .collect(Collectors.toList());
        return ResponseEntity.ok(articles);
    }

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