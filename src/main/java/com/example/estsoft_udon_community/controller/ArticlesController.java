package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.entity.request.AddArticleRequest;
import com.example.estsoft_udon_community.entity.response.ArticleResponse;
import com.example.estsoft_udon_community.entity.request.UpdateArticleRequest;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ArticlesController {
    private final ArticlesService articlesService;
    private final UsersService usersService;

    // 게시글 작성
//    @PostMapping("/locations/{locationId}/articles")
//    public ResponseEntity<Articles> addArticle(Authentication authentication, @RequestBody AddArticleRequest request) {
//        // 현재 로그인한 사용자의 이름
//        String username = authentication.getName();
//
//        // User
//        Users currentUser = usersService.findUserById(username);
//        Long locationId = currentUser.getLocation().getId();
//
//        Articles articles = articlesService.saveArticle(request, currentUser);
//        return ResponseEntity.created(URI.create("/api/locations/" + locationId + "/articles/" + articles.getId()))
//                .body(articles);
//    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> findAll() {
        List<ArticleResponse> articles = articlesService.findAll();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> findByArticleId(@PathVariable Long id) {
        Optional<ArticleResponse> article = articlesService.findByArticleId(id);
        return article.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<Articles> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        Articles updateArticle = articlesService.updateArticle(id, request);
        return ResponseEntity.ok(updateArticle);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteByAritlceId(@PathVariable Long id) {
        articlesService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/locations/{locationId}/articles")
    public ResponseEntity<List<ArticleResponse>> findByLocationId(@PathVariable Long locationId) {
        List<ArticleResponse> locationByIdArticle = articlesService.findByLocationId(locationId);
        return ResponseEntity.ok(locationByIdArticle);
    }

    @GetMapping("/articles/likes")
    public ResponseEntity<List<ArticleResponse>> findAllByLikes() {
        List<ArticleResponse> articlesByLikes = articlesService.findAllByLikes();
        return ResponseEntity.ok(articlesByLikes);
    }
}