package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.CommentsRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.CommentsResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.service.*;
import com.example.estsoft_udon_community.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ViewCommentController {
    private final ArticlesService articlesService;
    private final ArticlesLikeService articlesLikeService;
    private final CommentsService commentsService;
    private final CommentsLikeService commentsLikeService;
    private final UsersService usersService;

    // 게시글 조회 / 댓글 조회
    @GetMapping("/articles/{id}")
    public String boardDetail(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Model model) {
        Optional<ArticleResponse> article = articlesService.findByArticleId(id);
        Long articleLikeCount = articlesLikeService.getLikeCountForArticle(id);

        List<CommentsResponse> allComments = commentsService.findCommentsByArticleId(id).stream().map(CommentsResponse::new).toList();

        List<CommentsResponse> bestComments = allComments.stream()
                .peek(comment -> comment.setLikeCount(commentsLikeService.countCommentsLike(comment.getId())))
                .filter(comment -> comment.getLikeCount() > 0)
                .sorted((c1, c2) -> Long.compare(c2.getLikeCount(), c1.getLikeCount())) // 좋아요 수 기준 내림차순 정렬
                .limit(3) // 상위 3개만 추출
                .toList(); // List로 변환

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentsResponse> commentsPage = commentsService.findCommentsByArticleId(id, pageable);

        commentsPage = commentsPage.map(comment -> {
            comment.setLikeCount(commentsLikeService.countCommentsLike(comment.getId()));
            return comment;
        });

        if (!bestComments.isEmpty()) {
            model.addAttribute("bestCommentsPage", bestComments);
        }

        if (!commentsPage.isEmpty()) {
            model.addAttribute("commentsPage", commentsPage);
        }
        model.addAttribute("articleLikeCount", articleLikeCount);
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

            comment.setUsers(usersService.findUserById(SecurityUtil.getLoggedInUser().getId()));

            // 댓글 저장
            commentsService.saveComment(articleId, new CommentsRequest(comment));

            // 댓글이 추가된 후 해당 게시글로 리다이렉트
            return "redirect:/articles/" + articleId;
        } else {
            // 게시글이 없으면 게시판 목록으로 리다이렉트
            return "redirect:/articles";
        }
    }
}
