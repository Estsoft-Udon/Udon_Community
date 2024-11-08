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
import org.springframework.web.bind.annotation.*;

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

        // 정렬 기준 파라미터를 처리
        String[] sortParams = sort.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        // 검색어가 있을 경우 키워드를 포함하는 제목을 검색
        Page<ArticleResponse> articles;
        if (keyword != null && !keyword.isEmpty()) {
            // 검색어가 있는 경우, 제목에 검색어가 포함된 게시글만 검색
            articles = adminArticleService.findByTitleContainingAndIsDeleted(keyword, pageable);
        } else {
            // 검색어가 없으면 모든 게시글 조회
            articles = adminArticleService.getAdminArticles(pageable);
        }

        // 뷰에 필요한 데이터 추가
        model.addAttribute("articles", articles);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort); // 현재 정렬 기준을 뷰에서 사용할 수 있도록 전달

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
