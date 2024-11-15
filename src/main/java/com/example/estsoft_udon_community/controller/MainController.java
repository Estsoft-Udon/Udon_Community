package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.response.ArticleDetailResponse;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.PasswordHint;
import com.example.estsoft_udon_community.service.ArticlesService;
import com.example.estsoft_udon_community.service.LocationService;
import com.example.estsoft_udon_community.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.estsoft_udon_community.util.SecurityUtil.getLoggedInUser;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final LocationService locationService;
    private final ArticlesService articlesService;
    private final UsersService usersService;

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
        List<ArticleDetailResponse> newestPosts = articlesService.findTop5(0,5, "createdAt")
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())) // 최신 순 정렬
                .limit(5)
                .collect(Collectors.toList());

        model.addAttribute("articles", newestPosts); // 모델에 게시글 리스트 추가

        // 한뚝배기 게시글 5개 가져오기
        String userLocationName = getLoggedInUser() != null ? usersService.findUserById(getLoggedInUser().getId()).getLocation().getName() : "기본 지역 이름";
        List<ArticleDetailResponse> hotArticles = articlesService.findByHotArticles(userLocationName)
                .stream()
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("hotArticles", hotArticles);

        // 로그인 사용자의 정보
        if(getLoggedInUser() != null) {
            Users users = usersService.findUserById(getLoggedInUser().getId());
            model.addAttribute("user", users);
            model.addAttribute("passwordHints", PasswordHint.values());

            Location location = users.getLocation();
            model.addAttribute("locationId", location.getName());
            model.addAttribute("selectedUpperLocation", location.getUpperLocation());
        }
        Map<Users, Long> topUserMap = usersService.getTopUsersByLikes(5);

        model.addAttribute("topUserMap", topUserMap);

        getTopUsers(model);
        return "index";
    }

    // HOT한 우동
    public void getTopUsers(Model model) {
        Map<Users, Long> topUserMap = usersService.getTopUsersByLikes(5);
        model.addAttribute("topUserMap", topUserMap);
    }
}