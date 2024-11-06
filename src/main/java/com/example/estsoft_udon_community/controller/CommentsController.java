package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.CommentsArticlesResponse;
import com.example.estsoft_udon_community.dto.request.CommentsRequest;
import com.example.estsoft_udon_community.dto.response.CommentsResponse;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentsController {
    private final ArticlesService articlesService;
    private final CommentsService commentsService;
    private final ArticlesRepository articlesRepository;

    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentsResponse> saveCommentByArticleId(@PathVariable Long articleId,
                                                                   @RequestBody CommentsRequest request) {
        Comments comments = commentsService.saveComment(articleId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentsResponse(comments));
    }

    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentsArticlesResponse> findCommentsByArticleId(@PathVariable Long articleId) {

        Articles articles = articlesRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 id에 해당하는 게시글이 없습니다."));

        List<CommentsResponse> commentsResponseList = getCommentsByArticleId(articleId);

        return ResponseEntity.ok(
                new CommentsArticlesResponse(articles, commentsResponseList));
    }

    @GetMapping("/api/articles/{articleId}/commentsonly")
    public ResponseEntity<List<CommentsResponse>> findOnlyCommentsByArticleId(@PathVariable Long articleId) {
        List<CommentsResponse> commentsResponseList = getCommentsByArticleId(articleId);

        return ResponseEntity.ok(commentsResponseList);
    }

//     코멘트 수정
    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public ResponseEntity<CommentsResponse> updateComment(@PathVariable Long articleId,
                                                          @PathVariable Long commentId,
                                                          @RequestBody CommentsRequest reqeust) {
        Comments updatedComment = commentsService.update(commentId, reqeust);

        return ResponseEntity.ok(new CommentsResponse(updatedComment));
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentsService.deleteBy(commentId);
        return ResponseEntity.ok().build();
    }

    // 코멘트 삭제
    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public ResponseEntity<Void> softDelete(@PathVariable Long articleId, @PathVariable Long commentId) {
        System.out.println("실행은 됐다");
        commentsService.softDelete(commentId);

        return ResponseEntity.ok().build();
    }

    public List<CommentsResponse> getCommentsByArticleId(Long articleId) {
        Articles articles = articlesRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 id에 해당하는 게시글이 없습니다."));

        List<Comments> commentsList = commentsService.findCommentsByArticleId(articleId);

        List<CommentsResponse> commentsResponseList = commentsList.stream()
                .filter(comment -> !comment.getIsDeleted()) // 삭제되지 않은 댓글만 필터링
                .map(CommentsResponse::new)
                .toList();

        return commentsResponseList;
    }


}
/*
댓글 추가	POST	/api/articles/{articleId}/comments
댓글목록조회	GET	/api/articles/{articleId}/comments
댓글 수정	PUT	/api/comments/{commentId}
댓글 삭제	DELETE	/api/comments/{commentId}
 */