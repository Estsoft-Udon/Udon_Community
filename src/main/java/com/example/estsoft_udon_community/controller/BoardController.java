package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleDetailResponse;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.service.LocationService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

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
    public String getBoardList(@RequestParam(required = false) Long locationId, Model model) {

        List<ArticleDetailResponse> articles;
        if (locationId != null) {

            // location 정보가 있으면 지역별 게시글 나열
            Location locationById = locationService.getLocationById(locationId);
            model.addAttribute("location", locationById);

            articles = articlesService.findByLocationId(locationId);
        } else {
            // location 정보가 없을면 전체 게시글 날짜 순서대로
            model.addAttribute("location", null);
            articles = articlesService.findAll();
        }
        model.addAttribute("articles", articles);

        // 인기 해시태그 리스트 설정
        List<HashtagService.PopularHashtag> topHashtags = hashtagService.getTopUsedHashtags();
        model.addAttribute("topHashtags", topHashtags);

        return "board/board_list";
    }

    // 게시글(단건) 조회 articleId
    @GetMapping("articles/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
        Optional<ArticleResponse> article = articlesService.findByArticleId(id);
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
            return "board/board_detail";
        } else {
            return "redirect:/board_list";
        }
    }

    // 게시글 생성
    @GetMapping("/articles/new")
    public String boardEdit(Model model) {
        Articles article = new Articles();
        AddArticleRequest addArticleRequest = new AddArticleRequest(null, null, "", null, "", null);

        model.addAttribute("article", article);
        model.addAttribute("addArticleRequest", addArticleRequest);

        // 상위 지역 목록을 가져와서 모델에 추가
        List<String> upperLocations = locationService.getDistinctUpperLocations();
        model.addAttribute("upperLocations", upperLocations);

        // 첫 번째 상위 지역에 대한 하위 지역 목록을 초기화하여 모델에 추가
        if (!upperLocations.isEmpty()) {
            String firstUpperLocation = upperLocations.get(0);
            List<Location> lowerLocations = locationService.getLowerLocation(firstUpperLocation);
            model.addAttribute("locations", lowerLocations);
        }

        return "board/board_edit";
    }

//    @GetMapping("/articles/new")
//    public String boardEdit(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String loginId = authentication.getName();
//
//        Location userLocation = usersService.getUserLocationByLoginId(loginId);
//
//        Articles article = new Articles();
//        AddArticleRequest addArticleRequest = new AddArticleRequest(null, null, "", null, "", userLocation);
//
//        model.addAttribute("article", article);
//        model.addAttribute("addArticleRequest", addArticleRequest);
//        model.addAttribute("userLocation", userLocation.getName());
//
//        return "board/board_edit";
//    }
}
