package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.request.UpdateArticleRequest;
import com.example.estsoft_udon_community.service.ArticlesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<ArticleResponse>> findAll() {
        List<ArticleResponse> articles = articlesService.findAll();
        return ResponseEntity.ok(articles);
    }

    // 특정 게시글 조회
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
    public ResponseEntity<Void> deleteByAritlceId(@PathVariable Long id) {
        articlesService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/locations/{locationId}/articles")
//    public ResponseEntity<List<ArticleResponse>> findByLocationId(@PathVariable Long locationId) {
//        List<ArticleResponse> locationByIdArticle = articlesService.findByLocationId(locationId);
//        return ResponseEntity.ok(locationByIdArticle);
//    }
//
//    @GetMapping("/articles/likes")
//    public ResponseEntity<List<ArticleResponse>> findAllByLikes() {
//        List<ArticleResponse> articlesByLikes = articlesService.findAllByLikes();
//        return ResponseEntity.ok(articlesByLikes);
//    }
}