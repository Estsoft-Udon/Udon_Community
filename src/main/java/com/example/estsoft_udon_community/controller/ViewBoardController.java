package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.CommentsArticlesResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.service.ArticlesLikeService;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.CommentsLikeService;
import com.example.estsoft_udon_community.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ViewBoardController {
    private final ArticlesService articlesService;
    private final CommentsService commentsService;
    private final ArticlesLikeService articlesLikeService;
    private final CommentsLikeService commentsLikeService;

//    @GetMapping("/articles")
//    public String getBoardList(Model model) {
//        List<ArticleResponse> articles = articlesService.findAll();
//        model.addAttribute("articles", articles);
//        return "board/board_list";
//    }
//
//    @GetMapping("/articles/{id}")
//    public String boardDetail(@PathVariable Long id, Model model) {
//        Optional<ArticleResponse> article = articlesService.findByArticleId(id);
//        if (article.isPresent()) {
//            model.addAttribute("article", article.get());
//            return "board/board_detail";
//        } else {
//            return "redirect:/board_list";
//        }
//    }
//
//    @GetMapping("/articles/new")
//    public String createArticle(Model model) {
//        Articles article = new Articles();
//
//        AddArticleRequest addArticleRequest = new AddArticleRequest(null, null, "", null, "", null);
//
//        model.addAttribute("article", article);
//        model.addAttribute("addArticleRequest", addArticleRequest);
//
//        return "board/board_edit";
//    }

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
//            model.addAttribute("commentsLikeCount", )

            return "board/board_detail";
        }
        return "redirect:/board_list";
    }
}