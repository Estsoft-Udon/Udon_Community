package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.ArticlesLikeRequest;
import com.example.estsoft_udon_community.dto.request.CommentsLikeRequest;
import com.example.estsoft_udon_community.dto.response.ArticleWithLikeResponse;
import com.example.estsoft_udon_community.dto.response.CommentsResponse;
import com.example.estsoft_udon_community.dto.response.CommentsWithLikeResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.ArticlesLike;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.CommentsLike;
import com.example.estsoft_udon_community.service.ArticlesLikeService;
import com.example.estsoft_udon_community.service.CommentsLikeService;
import com.example.estsoft_udon_community.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {
    private final ArticlesLikeService articlesLikeService;
    private final CommentsLikeService commentsLikeService;
    private final CommentsService commentsService;

    // 코멘트 좋아요 등록 / 삭제
    @PostMapping("/commentsLike")
    public ResponseEntity<Void> pressCommentsLike(@RequestBody CommentsLikeRequest request) {
        CommentsLike commentsLike = commentsLikeService.pressCommentsLike(request.getCommentId(), request.getUserId());
        if(commentsLike == null) {
            // 좋아요가 삭제된 경우
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }
        // 좋아요가 추가된 경우 (존재하던 좋아요를 클릭했을 때)
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    // 게시글 좋아요 등록 / 삭제
    @PostMapping("/articlesLike")
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

    // 게시글 좋아요 순으로 출력하기
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleWithLikeResponse>> findArticlesOrderByLikesCountDesc() {
        List<Object[]> articlesDataList = articlesLikeService.findArticlesOrderByLikesCountDesc();

        List<ArticleWithLikeResponse> articleByLikeResponseList = articlesDataList.stream()
                .map(data -> new ArticleWithLikeResponse((Articles) data[0], (Long) data[1])) // Articles와 좋아요 수 추출
                .toList();

        return ResponseEntity.ok(articleByLikeResponseList);
    }

    // 코멘트 좋아요 순으로 출력하기
    @GetMapping("/comments")
    public ResponseEntity<List<CommentsResponse>> findCommentsOrderByLikesCountDesc() {
        List<Comments> commentsList = commentsLikeService.findCommentsOrderByLikesCountDesc();

        List<CommentsResponse> commentsResponseList = commentsList.stream()
                .map(CommentsResponse::new)
                .toList();

        return ResponseEntity.ok(commentsResponseList);
    }

    @GetMapping("/comments/{articleId}")
    public ResponseEntity<List<CommentsWithLikeResponse>> findCommentsOrderByLikesCount(@PathVariable Long articleId) {
        List<Object[]> commentsDataList = commentsLikeService.findCommentsByArticleIdOrderByLikesCountDesc(articleId);

        List<CommentsWithLikeResponse> commentsWithLikeResponseList = commentsDataList.stream()
                .map(data -> new CommentsWithLikeResponse((Comments) data[0], (Long) data[1]))
                .toList();

        return ResponseEntity.ok(commentsWithLikeResponseList);
    }
}
