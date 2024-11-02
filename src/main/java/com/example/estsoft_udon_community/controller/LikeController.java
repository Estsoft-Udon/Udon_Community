package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.ArticlesLikeRequest;
import com.example.estsoft_udon_community.dto.request.CommentsLikeRequest;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.ArticlesLike;
import com.example.estsoft_udon_community.entity.CommentsLike;
import com.example.estsoft_udon_community.service.ArticlesLikeService;
import com.example.estsoft_udon_community.service.CommentsLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {
    private final ArticlesLikeService articlesLikeService;
    private final CommentsLikeService commentsLikeService;

    @PostMapping("/CommentsLike")
    public ResponseEntity<Void> pressCommentsLike(@RequestBody CommentsLikeRequest request) {
        CommentsLike commentsLike = commentsLikeService.pressCommentsLike(request.getCommentId(), request.getUserId());
        if(commentsLike == null) {
            // 좋아요가 삭제된 경우
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }
        // 좋아요가 추가된 경우 (존재하던 좋아요를 클릭했을 때)
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    @PostMapping("/ArticlesLike")
    public ResponseEntity<Void> pressArticlesLike(@RequestBody ArticlesLikeRequest request) {
        // ArticlesLike를 추가하거나 삭제하는 서비스 메서드 호출
        ArticlesLike articlesLike = articlesLikeService.pressArticlesLike(request.getArticleId(), request.getUserId());

        if (articlesLike == null) {
            // 좋아요가 삭제된 경우 (존재하지 않던 좋아요를 클릭했을 때)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }

        // 좋아요가 추가된 경우 (존재하던 좋아요를 클릭했을 때)
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

}
