package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.service.admin.AdminArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/board")
public class AdminBoardViewController {

    private final AdminArticleService adminArticleService;

    public AdminBoardViewController(AdminArticleService adminArticleService) {
        this.adminArticleService = adminArticleService;
    }

    // 게시글 목록
    @GetMapping("/board_list")
    public String boardList(Model model) {
//        List<ArticleResponse> article;
//        model.addAttribute("articles", adminArticleService);
        List<ArticleResponse> articles = adminArticleService.getAdminArticles();
        model.addAttribute("articles", articles);
        return "admin/board/board_list";
    }

    // 게시글 수정(공개/비공개)
    @GetMapping("/board_edit")
    public String boardEdit() {

        return "admin/board/board_edit";
    }
}
