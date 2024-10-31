package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.entity.dto.AddArticleRequest;
import com.example.estsoft_udon_community.service.ArticlesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ArticlesController {
    private final ArticlesService articlesService;
    private final UsersService usersService;

    @PostMapping("/locations/{locationId}/articles")
    public ResponseEntity<Articles> addArticle(Authentication authentication, @RequestBody AddArticleRequest request) {
        String username = authentication.getName();
        Users currentUser = usersService.findByUsername(username);

        Long locationId = currentUser.getLocation().getId();

        Articles articles = articlesService.saveArticle(request, currentUser);
        return ResponseEntity.created(URI.create("/api/locations/" + locationId + "/articles/" + articles.getId()))
                .body(articles);
    }
}