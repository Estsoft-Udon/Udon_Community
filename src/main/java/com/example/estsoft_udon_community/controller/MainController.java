package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.response.ArticleDetailResponse;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.EventService;
import com.example.estsoft_udon_community.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private final EventService eventService;
    private final LocationService locationService;
    private final ArticlesService articlesService;

    public MainController(EventService eventService, LocationService locationService, ArticlesService articlesService) {
        this.eventService = eventService;
        this.locationService = locationService;
        this.articlesService = articlesService;
    }

    // index 페이지 메서드
    @GetMapping("/")
    public String index(Model model) {
        // 상위 지역 목록을 가져와서 모델에 추가
        List<String> upperLocations = locationService.getDistinctUpperLocations();
        model.addAttribute("upperLocations", upperLocations);

        // 첫 번째 상위 지역에 대한 하위 지역 목록을 초기화하여 모델에 추가
        if (!upperLocations.isEmpty()) {
            String firstUpperLocation = upperLocations.get(0);
            List<Location> lowerLocations = locationService.getLowerLocations(firstUpperLocation);
            model.addAttribute("locations", lowerLocations);
        }

        // 최신 게시글 5개 가져오기
        List<ArticleDetailResponse> newestPosts = articlesService.findAll(0,5)
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())) // 최신 순 정렬
                .limit(5)
                .collect(Collectors.toList());


        model.addAttribute("articles", newestPosts); // 모델에 게시글 리스트 추가

        return "index";
    }
}