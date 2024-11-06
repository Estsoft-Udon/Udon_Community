package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleDetailResponse;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.CommentsResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.security.CustomUserDetails;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.HashtagService;
import com.example.estsoft_udon_community.util.SecurityUtil;
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

    @GetMapping("/articles/search")
    public String searchArticles(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Page<ArticleDetailResponse> articles = articlesService.searchByTitle(title, page, size);

        model.addAttribute("articles", articles);
        model.addAttribute("searchQuery", title);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articles.getTotalPages());
        model.addAttribute("totalItems", articles.getTotalElements());

        // 인기 해시태그 리스트 설정
        List<HashtagService.PopularHashtag> topHashtags = hashtagService.getTopUsedHashtags();
        model.addAttribute("topHashtags", topHashtags);

        return "board/board_list";
    }

//    // 게시글 생성
//    @GetMapping("/articles/new")
//    public String boardEdit(Model model) {
//        Articles article = new Articles();
//        AddArticleRequest addArticleRequest = new AddArticleRequest(null, null, "", null, "", null);
//
//        model.addAttribute("article", article);
//        model.addAttribute("addArticleRequest", addArticleRequest);
//
//        // 상위 지역 목록을 가져와서 모델에 추가
//        List<String> upperLocations = locationService.getDistinctUpperLocations();
//        model.addAttribute("upperLocations", upperLocations);
//
//        // 첫 번째 상위 지역에 대한 하위 지역 목록을 초기화하여 모델에 추가
//        if (!upperLocations.isEmpty()) {
//            String firstUpperLocation = upperLocations.get(0);
//            List<Location> lowerLocations = locationService.getLowerLocation(firstUpperLocation);
//            model.addAttribute("locations", lowerLocations);
//        }
//
//        return "board/board_edit";
//    }

    @PutMapping("/articles/")
    public String boardEdit(Model model) {
        String loginId = SecurityUtil.getLoggedInUser().getLoginId();



        return "board/board_edit";
    }
}
