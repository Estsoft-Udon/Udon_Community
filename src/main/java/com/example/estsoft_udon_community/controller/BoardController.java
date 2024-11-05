package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.service.ArticlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.service.LocationService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final ArticlesService articlesService;
    private final LocationService locationService;

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

    @GetMapping("/board_edit")
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
