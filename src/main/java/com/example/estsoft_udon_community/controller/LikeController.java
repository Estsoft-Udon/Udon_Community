package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.ArticlesLikeRequest;
import com.example.estsoft_udon_community.dto.request.CommentsLikeRequest;
import com.example.estsoft_udon_community.dto.response.ArticleWithLikeResponse;
import com.example.estsoft_udon_community.dto.response.CommentsResponse;
import com.example.estsoft_udon_community.dto.response.CommentsWithLikeResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.service.ArticlesLikeService;
import com.example.estsoft_udon_community.service.CommentsLikeService;
import com.example.estsoft_udon_community.service.UsersService;
import com.example.estsoft_udon_community.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {
    private final ArticlesLikeService articlesLikeService;
    private final CommentsLikeService commentsLikeService;
    private final UsersService usersService;

    // 코멘트 좋아요 등록 / 삭제
    @PostMapping("/commentsLike")
    public ResponseEntity<Map<String, Object>> pressCommentsLike(@RequestBody CommentsLikeRequest request) {
        request.setUserId(usersService.findUserById(SecurityUtil.getLoggedInUser().getId()).getId());
        Long newLikeCount = commentsLikeService.pressCommentsLike(request.getCommentId(), request.getUserId());

        Map<String, Object> response = new HashMap<>();
        response.put("newLikeCount", newLikeCount);
        response.put("success", true);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 게시글 좋아요 등록 / 삭제
    @PostMapping("/articlesLike")
    public ResponseEntity<Map<String, Object>> pressArticlesLike(@RequestBody ArticlesLikeRequest request) {
        request.setUserId(usersService.findUserById(SecurityUtil.getLoggedInUser().getId()).getId());
        Long newLikeCount = articlesLikeService.pressArticlesLike(request.getArticleId(), request.getUserId());

        Map<String, Object> response = new HashMap<>();
        response.put("newLikeCount", newLikeCount);
        response.put("success", true);

        return ResponseEntity.status(HttpStatus.OK).body(response);
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
