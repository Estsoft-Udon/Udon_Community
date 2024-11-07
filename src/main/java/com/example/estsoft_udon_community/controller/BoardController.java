package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleDetailResponse;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.CommentsResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import com.example.estsoft_udon_community.enums.UpperLocationEnum;
import com.example.estsoft_udon_community.security.CustomUserDetails;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.service.LocationService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final ArticlesService articlesService;
    private final LocationService locationService;
    private final HashtagService hashtagService;

    // 게시글 리스트 조회 (전체 or 동네별)
    @GetMapping("/articles")
    public String getBoardList(@RequestParam(required = false) Long locationId,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model) {

        // 페이지네이션을 위한 서비스 호출
        Page<ArticleDetailResponse> articles;

        if (locationId != null) {
            // location 정보가 있으면 지역별 게시글 나열
            Location locationById = locationService.getLocationById(locationId);
            articles = articlesService.findByLocationId(locationId, page, size);
            model.addAttribute("location", locationById);

        } else {
            // 지역 정보가 없을 때 - 전체 지역 게시글 리스트
            articles = articlesService.findAll(page, size);
            model.addAttribute("location", null);
        }
        model.addAttribute("articles", articles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articles.getTotalPages());
        model.addAttribute("totalItems", articles.getTotalElements());

        // location 정보가 있든 없든 있어야 하는 topHashtags 정보
        List<HashtagService.PopularHashtag> topHashtags = hashtagService.getTopUsedHashtags();
        model.addAttribute("topHashtags", topHashtags);

        return "board/board_list";
    }

    // 게시글 검색
    @GetMapping("/articles/search")
    public String searchArticles(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Page<ArticleDetailResponse> articles = articlesService.searchByTitle(title, page, size);

        model.addAttribute("searchQuery", title);

        model.addAttribute("articles", articles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articles.getTotalPages());
        model.addAttribute("totalItems", articles.getTotalElements());

        // 인기 해시태그 리스트 설정
        List<HashtagService.PopularHashtag> topHashtags = hashtagService.getTopUsedHashtags();
        model.addAttribute("topHashtags", topHashtags);

        return "board/board_list";
    }

    // 해시태그로 게시글 조회
    @GetMapping("/articles/hashtag/{hashtagId}")
    public String getArticlesByHashtag(@PathVariable Long hashtagId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       Model model) {

        Page<ArticleDetailResponse> articles = articlesService.findByHashtag(hashtagId, page, size);

        model.addAttribute("articles", articles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articles.getTotalPages());
        model.addAttribute("totalItems", articles.getTotalElements());

        List<HashtagService.PopularHashtag> topHashtags = hashtagService.getTopUsedHashtags();
        model.addAttribute("topHashtags", topHashtags);

        return "board/board_list";
    }

    // 카테고리별 게시글 조회
    @GetMapping("/articles/category/{category}")
    public String getArticlesByCategory(@PathVariable String category,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        Model model) {

        Page<ArticleDetailResponse> articles = articlesService.findByCategory(category, page, size);

        model.addAttribute("articles", articles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articles.getTotalPages());
        model.addAttribute("totalItems", articles.getTotalElements());

        List<HashtagService.PopularHashtag> topHashtags = hashtagService.getTopUsedHashtags();
        model.addAttribute("topHashtags", topHashtags);

        return "board/board_list";
    }

    // 게시글 생성
    @GetMapping("/articles/new")
    public String createBoard(Model model) {

        model.addAttribute("articleCategories", ArticleCategory.values());

        // 상위 지역 목록을 가져와서 모델에 추가
        List<String> upperLocations = locationService.getDistinctUpperLocations();
        model.addAttribute("upperLocations", upperLocations);

        // 첫 번째 상위 지역에 대한 하위 지역 목록을 초기화하여 모델에 추가
        if (!upperLocations.isEmpty()) {
            String firstUpperLocation = upperLocations.get(0);
            List<Location> lowerLocations = locationService.getLowerLocations(firstUpperLocation);
            model.addAttribute("locations", lowerLocations);
        }

        return "board/board_edit";
    }

    // 게시글 생성
    @PostMapping("/articles/new")
    public String createBoard(Model model, @ModelAttribute AddArticleRequest addArticleRequest,
                            String upperLocation, String locationName) {

        model.addAttribute("article", addArticleRequest);
        model.addAttribute("articleCategories", ArticleCategory.values());

        //article 저장
        articlesService.saveArticle(addArticleRequest, Long.valueOf(locationName));

        return "board/board_edit";
    }

    // 게시글 수정
    @GetMapping("/articles/edit/{articleId}")
    public String boardEdit(@PathVariable Long articleId, Model model) {
        setCategoriesAndLocations(model);

        // 기존 게시글 데이터를 조회하여 모델에 추가
        ArticleResponse article = articlesService.findByArticleId(articleId)
                .orElseThrow(() -> new IllegalArgumentException("article이 존재하지 않습니다."));
        model.addAttribute("article", article);

        Location location = locationService.findByName(article.getLocation());
        model.addAttribute("originLocation", location);

        return "board/board_edit";
    }

    // 게시글 수정
    @PostMapping("/articles/edit/{articleId}")
    public String boardEdit(@PathVariable Long articleId, Model model,
                            @ModelAttribute AddArticleRequest request,
                            String locationName) {

        model.addAttribute("article", request);
        model.addAttribute("articleCategories", ArticleCategory.values());

        Location location = locationService.findByName(locationName);
        request.setLocationId(location.getId());

        //article 저장
        articlesService.updateArticle2(articleId, request);

        return "board/board_edit";
    }

    // 카테고리 및 로케이션 세팅
    public void setCategoriesAndLocations(Model model) {
        model.addAttribute("articleCategories", ArticleCategory.values());

        // 상위 지역 목록을 가져와서 모델에 추가
        List<String> upperLocations = locationService.getDistinctUpperLocations();
        model.addAttribute("upperLocations", upperLocations);

        // 첫 번째 상위 지역에 대한 하위 지역 목록을 초기화하여 모델에 추가
        if (!upperLocations.isEmpty()) {
            String firstUpperLocation = upperLocations.get(0);
            List<Location> lowerLocations = locationService.getLowerLocation(firstUpperLocation);
            model.addAttribute("locations", lowerLocations);
        }
    }
}
