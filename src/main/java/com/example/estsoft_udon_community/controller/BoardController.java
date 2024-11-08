package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleDetailResponse;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.entity.Hashtag;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import com.example.estsoft_udon_community.service.ArticleHashtagService;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.HashtagService;
import com.example.estsoft_udon_community.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.service.LocationService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static com.example.estsoft_udon_community.util.SecurityUtil.getLoggedInUser;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final ArticlesService articlesService;
    private final LocationService locationService;
    private final HashtagService hashtagService;
    private final ArticleHashtagService articleHashtagService;

    // 게시글 리스트 조회 (전체 or 동네별)
    @GetMapping("/articles")
    public String getBoardList(@RequestParam(required = false) Long locationId,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "createdAt") String sortOption,
                               Model model) {

        Page<ArticleDetailResponse> articles;
        if (locationId != null) {
            Location locationById = locationService.getLocationById(locationId);
            articles = articlesService.findByLocationId(locationId, page, size, sortOption);
            model.addAttribute("location", locationById);
        } else {
            articles = articlesService.findAll(page, size, sortOption);
            model.addAttribute("location", null);
        }

        setArticleModel(model, articles, page);

        return "board/board_list";
    }

    // 게시글 검색
    @GetMapping("/articles/search")
    public String searchArticles(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortOption,
            Model model) {

        Page<ArticleDetailResponse> articles = articlesService.searchByTitle(title, page, size, sortOption);

        model.addAttribute("searchQuery", title);

        setArticleModel(model, articles, page);

        return "board/board_list";
    }

    // 해시태그로 게시글 조회
    @GetMapping("/articles/hashtag/{hashtagId}")
    public String getArticlesByHashtag(@PathVariable Long hashtagId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(defaultValue = "createdAt") String sortOption,
                                       Model model) {

        Page<ArticleDetailResponse> articles = articlesService.findByHashtag(hashtagId, page, size, sortOption);

        setArticleModel(model, articles, page);

        return "board/board_list";
    }

    // 카테고리별 게시글 조회
    @GetMapping("/articles/category/{category}")
    public String getArticlesByCategory(@PathVariable String category,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(defaultValue = "createdAt") String sortOption,
                                        Model model) {

        Page<ArticleDetailResponse> articles = articlesService.findByCategory(category, page, size, sortOption);

        setArticleModel(model, articles, page);

        return "board/board_list";
    }

    public void setArticleModel(Model model, Page<ArticleDetailResponse> articles, int page) {
        model.addAttribute("articles", articles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articles.getTotalPages());
        model.addAttribute("totalItems", articles.getTotalElements());

        List<HashtagService.PopularHashtag> topHashtags = hashtagService.getTopUsedHashtags();
        model.addAttribute("topHashtags", topHashtags);
    }

    // 한뚝배기
    @GetMapping("/articles/hotRestaurant")
    public String getHotRestaurantArticles(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(defaultValue = "createdAt") String sortOption,
                                           Model model) {
        // 접속 중인 유저 확인
        Users loggedInUser = getLoggedInUser();
        if (loggedInUser == null) {
            throw new IllegalStateException("로그인된 유저가 없습니다.");
        }
        Location userLocation = loggedInUser.getLocation();

        // 카테고리와 위치를 기준으로 인기 맛집 게시글 조회
        Page<ArticleDetailResponse> hotRestaurantArticles = articlesService.findHotRestaurantArticlesForCurrentUser(
                userLocation, page, size, sortOption);

        model.addAttribute("currentPageContext", "hotRestaurant");
        // 조회된 게시글 정보를 모델에 추가
        model.addAttribute("articles", hotRestaurantArticles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hotRestaurantArticles.getTotalPages());
        model.addAttribute("totalItems", hotRestaurantArticles.getTotalElements());

        List<HashtagService.PopularHashtag> topHashtags = hashtagService.getTopUsedHashtags();
        model.addAttribute("topHashtags", topHashtags);

        return "board/board_list";
    }

    // 게시글 생성
    @GetMapping("/articles/new")
    public String createBoard(Model model) {
        ModelUtil modelUtil = new ModelUtil(locationService);
        modelUtil.setCategoriesAndLocations(model);

        return "board/board_edit";
    }

    // 게시글 생성
    @PostMapping("/articles/new")
    public String createBoard(Model model, @RequestBody AddArticleRequest request) {
        model.addAttribute("article", request);
        model.addAttribute("articleCategories", ArticleCategory.values());

        //article 저장
        articlesService.saveArticle(request, request.getLocationId());

        return "board/board_edit";
    }

    // 게시글 수정
    @GetMapping("/articles/edit/{articleId}")
    public String editBoard(@PathVariable Long articleId, Model model) {
        ModelUtil modelUtil = new ModelUtil(locationService);
        modelUtil.setCategoriesAndLocations(model);

        // 기존 게시글 데이터를 조회하여 모델에 추가
        ArticleResponse article = articlesService.findByArticleId(articleId)
                .orElseThrow(() -> new IllegalArgumentException("article이 존재하지 않습니다."));

        Location location = article.getLocation();

        model.addAttribute("article", article);
        model.addAttribute("originLocation", location);
        System.out.println(model.getAttribute("locations"));

        return "board/board_edit";
    }

    // 게시글 수정
    @PostMapping("/articles/edit/{articleId}")
    public String editBoard(@PathVariable Long articleId, Model model,
                            @RequestBody AddArticleRequest request) {

        model.addAttribute("article", request);
        model.addAttribute("articleCategories", ArticleCategory.values());

        //article 저장
        articlesService.updateArticle2(articleId, request);

        return "board/board_edit";
    }

    @GetMapping("/article/{articleId}/hashtags")
    @ResponseBody
    public List<String> getHashtagsByArticleId(@PathVariable Long articleId) {
        return articleHashtagService.getHashtagsByArticleId(articleId).stream()
                .map(Hashtag::getName)  // Hashtag 객체에서 name을 추출
                .toList();  // List<String>으로 수집
    }
}
