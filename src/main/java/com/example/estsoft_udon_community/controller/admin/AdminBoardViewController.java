package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.admin.AdminArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/board")
public class AdminBoardViewController {

    private final AdminArticleService adminArticleService;
    private final ArticlesService articlesService;

    public AdminBoardViewController(AdminArticleService adminArticleService, ArticlesService articlesService) {
        this.adminArticleService = adminArticleService;
        this.articlesService = articlesService;
    }

    // 게시글 목록
    @GetMapping("/board_list")
    public String boardList(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort,
                            Model model) {

        String[] sortParams = sort.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<ArticleResponse> articles;
        if (keyword != null && !keyword.isEmpty()) {
            articles = adminArticleService.findByTitleContaining(keyword, pageable);
        } else {
            articles = adminArticleService.getAdminArticles(pageable);
        }

        model.addAttribute("articles", articles);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort); // 뷰에서 정렬 기준 유지
        return "admin/board/board_list";
    }



    // 게시글 수정(공개/비공개)
    @GetMapping("/board_edit/{id}")
    public String boardEdit(@PathVariable Long id, Model model) {
        ArticleResponse article = adminArticleService.getArticleById(id);
        model.addAttribute("article", article);
        return "admin/board/board_edit";
    }
}
