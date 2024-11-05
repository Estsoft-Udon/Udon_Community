package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.service.ArticlesService;
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

    @GetMapping("/articles")
    public String getBoardList(Model model) {
        List<ArticleResponse> articles = articlesService.findAll();
        model.addAttribute("articles", articles);
        return "board/board_list";
    }

    @GetMapping("/articles/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
        Optional<ArticleResponse> article = articlesService.findByArticleId(id);
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
            return "board/board_detail";
        } else {
            return "redirect:/board_list";
        }
    }

    @GetMapping("/articles/new")
    public String createArticle(Model model) {
        Articles article = new Articles();

        AddArticleRequest addArticleRequest = new AddArticleRequest(null, null, "", null, "", null);

        model.addAttribute("article", article);
        model.addAttribute("addArticleRequest", addArticleRequest);

        return "board/board_edit";
    }
}