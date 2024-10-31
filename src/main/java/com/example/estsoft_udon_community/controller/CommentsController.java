package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.dto.CommentsArticlesResponse;
import com.example.estsoft_udon_community.entity.dto.CommentsRequest;
import com.example.estsoft_udon_community.entity.dto.CommentsResponse;
import com.example.estsoft_udon_community.service.CommentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentsController {
    private final ArticlesService articlesService;
    private final CommentsService commentsService;

    public CommentsController(ArticlesService articlesService, CommentsService commentsService) {
        this.articlesService = articlesService;
        this.commentsService = commentsService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<CommentsResponse> saveCommentByArticleId(@PathVariable Long articleId, @RequestBody CommentsRequest request) {
        Comments comments = commentsService.saveComment(articleId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentsResponse(comments));
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<CommentsArticlesResponse> getCommentByArticleId(@PathVariable Long articleId) {
        Articles articles = articlesService.findById(articleId);
        List<Comments> commentsList = commentsService.findCommentsByArticleId(articleId);

        List<CommentsResponse> commentsResponseList = commentsList.stream()
                .map(CommentsResponse::new).toList();

        return ResponseEntity.ok(new CommentsArticlesResponse(articles, commentsResponseList));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentsResponse> updateComment(@PathVariable Long commentId,
                                                          @RequestBody CommentsRequest reqeust) {
        Comments updatedComment = commentsService.update(commentId, reqeust);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deletedComment(@PathVariable Long commentId) {
        commentsService.deleteBy(commentId);
        return ResponseEntity.ok().build();
    }



}
/*
댓글 추가	POST	/api/articles/{articleId}/comments

댓글목록조회	GET	/api/articles/{articleId}/comments	200 OK	400 Bad Request,
404 Not Found
댓글 수정	PUT	/api/comments/{commentId}	200 OK	400 Bad Request,
404 Not Found
댓글 삭제	DELETE	/api/comments/{commentId}	204 NO Content	400 Bad Request,
404 Not Found

 */