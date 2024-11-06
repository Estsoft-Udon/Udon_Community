package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.request.CommentsRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.CommentsResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.security.CustomUserDetails;
import com.example.estsoft_udon_community.service.*;
import com.example.estsoft_udon_community.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ViewCommentController {
    private final ArticlesService articlesService;
    private final CommentsService commentsService;
    private final UsersService usersService;
    private final CommentsLikeService commentsLikeService;

    // 게시글 조회 / 댓글 조회
    @GetMapping("/articles/{id}")
    public String boardDetail(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Model model) {
        Optional<ArticleResponse> article = articlesService.findByArticleId(id);
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentsResponse> commentsPage = commentsService.findCommentsByArticleId(id, pageable);

        commentsPage = commentsPage.map(comment -> {
            comment.setLikeCount(commentsLikeService.countCommentsLike(comment.getId()));
            return comment;
        });

        if (!commentsPage.isEmpty()) {
            model.addAttribute("commentsPage", commentsPage);
        }
        model.addAttribute("loggedInUserId", SecurityUtil.getLoggedInUser().getId());

        if (article.isPresent()) {
            model.addAttribute("article", article.get());
            return "board/board_detail";
        } else {
            return "redirect:/board_list";
        }
    }

    // 코멘트 추가
    @PostMapping("/articles/{articleId}/comments")
    public String addComment(@PathVariable Long articleId, @RequestBody CommentsRequest request) {
        Articles article = articlesService.findJustArticle(articleId);
        if (article != null) {
            // 댓글 추가 로직
            Comments comment = new Comments();
            comment.setContent(request.getContent());
            comment.setArticles(article); // 게시글 ID 설정
            comment.setUsers(SecurityUtil.getLoggedInUser());

            // 댓글 저장
            commentsService.saveComment(articleId, new CommentsRequest(comment));

            // 댓글 추가 후 리다이렉션
            return "redirect:/articles/" + articleId; // 댓글이 추가된 후 해당 게시글로 리다이렉트
        } else {
            // 게시글이 존재하지 않을 경우 처리
            return "redirect:/articles"; // 게시글이 없으면 게시판 목록으로 리다이렉트
        }
    }
//    // 코멘트 수정
//    @PutMapping("/articles/{articleId}/comments/{commentId}")
//    public String editComment(@PathVariable Long articleId,
//                              @PathVariable Long commentId,
//                              @RequestBody CommentsRequest request) {
//        Comments comments = commentsService.findComment(commentId);
//        System.out.println(request.getContent());
//
//        if(comments != null) {
//            commentsService.update(commentId, request);
//        }
//
//        return "redirect:/articles/{articleId}";
//    }

//    // 코멘트 삭제 => REST API
//    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
//    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
//        Comments comments = commentsService.findComment(commentId);
//
//        if(comments != null) {
//            commentsService.softDelete(commentId);
//        }
//
//        return "redirect:/articles/" + articleId;
//    }
}
