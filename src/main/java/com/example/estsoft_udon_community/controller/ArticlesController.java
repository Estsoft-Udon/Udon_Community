package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleDetailResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.request.UpdateArticleRequest;
import com.example.estsoft_udon_community.service.ArticlesService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ArticlesController {
    private final ArticlesService articlesService;

    // 게시글 작성
    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody AddArticleRequest request) {
        Articles article = articlesService.saveArticle(request);
        return new ResponseEntity<>(new ArticleResponse(article), HttpStatus.CREATED);
    }

    // 게시글 전체 조회
    @GetMapping("/articles")
    public ResponseEntity<Page<ArticleDetailResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ArticleDetailResponse> articles = articlesService.findAll(page, size);
        return ResponseEntity.ok(articles);
    }

    // 특정 게시글 조회 articleId
    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> findByArticleId(@PathVariable Long id) {
        Optional<ArticleResponse> article = articlesService.findByArticleId(id);
        return article.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 특정 게시글 수정
    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        Articles updatedArticle = articlesService.updateArticle(id, request);
        ArticleResponse response = new ArticleResponse(updatedArticle);
        return ResponseEntity.ok(response);
    }

    // 특정 게시글 삭제
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteByArticleId(@PathVariable Long id) {
        articlesService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }

//    // 특정 지역 게시글 조회하기
//    @GetMapping("/locations/{locationId}/articles")
//    public ResponseEntity<Page<ArticleResponse>> findByLocationId(
//            @PathVariable Long locationId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Page<ArticleResponse> locationByIdArticle = articlesService.findByLocationId(locationId, page, size);
//        return ResponseEntity.ok(locationByIdArticle);
//    }

    // 해시태그로 게시글 조회
    @GetMapping("/articles/hashtag/{hashtagId}")
    public ResponseEntity<Page<ArticleDetailResponse>> findByHashtag(
            @PathVariable Long hashtagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ArticleDetailResponse> hashtagByArticle = articlesService.findByHashtag(hashtagId, page, size);
        return ResponseEntity.ok(hashtagByArticle);
    }

    // 카테고리로 게시글 조회
    @GetMapping("/articles/category/{category}")
    public ResponseEntity<Page<ArticleDetailResponse>> findByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ArticleDetailResponse> articles = articlesService.findByCategory(category, page, size);
        return ResponseEntity.ok(articles);
    }

    // 제목 검색 기능
    @GetMapping("/articles/search")
    public ResponseEntity<Page<ArticleDetailResponse>> searchArticlesByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Page.empty());
        }
        Page<ArticleDetailResponse> articles = articlesService.searchByTitle(title, page, size);
        return ResponseEntity.ok(articles);
    }
}