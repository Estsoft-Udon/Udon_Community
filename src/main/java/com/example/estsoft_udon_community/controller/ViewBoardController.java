package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.service.ArticlesLikeService;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ViewBoardController {
    private final ArticlesService articlesService;
    private final CommentsService commentsService;
    private final ArticlesLikeService articlesLikeService;

    @GetMapping("/board_detail/{articleId}")
    public String getArticle(@PathVariable Long articleId, Model model) {
        if(articlesService.findByArticleId(articleId).isPresent()) {

            ArticleResponse articles = articlesService.findByArticleId(articleId).get();
            List<Comments> comments = commentsService.findCommentsByArticleId(articleId);
            Long artilceLikeCount = articlesLikeService.getLikeCountForArticle(articleId);


            model.addAttribute("article", articles);
            if(comments != null) {
                model.addAttribute("comment", comments);
            }
            model.addAttribute("articleLikeCount", artilceLikeCount);

            return "board/board_detail";
        }
        return "redirect:/board_list";
    }
}