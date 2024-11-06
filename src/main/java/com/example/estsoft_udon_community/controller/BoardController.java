package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleDetailResponse;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.CommentsResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.security.CustomUserDetails;
import com.example.estsoft_udon_community.service.ArticlesService
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


//    // 게시글 조회 / 댓글 조회
//    @GetMapping("/articles/{id}")
//    public String boardDetail(@PathVariable Long id,
//                              @RequestParam(defaultValue = "0") int page,
//                              @RequestParam(defaultValue = "5") int size,
//                              @AuthenticationPrincipal CustomUserDetails customUserDetails,
//                              Model model) {
//        Optional<ArticleResponse> article = articlesService.findByArticleId(id);
//        Pageable pageable = PageRequest.of(page, size);
//        Page<CommentsResponse> commentsPage = commentsService.findCommentsByArticleId(id, pageable);
//
//        model.addAttribute("commentsPage", commentsPage);
//        model.addAttribute("loggedInUserId", getLoggedInUser(customUserDetails).getId());
//
//        if (article.isPresent()) {
//            model.addAttribute("article", article.get());
//            return "board/board_detail";
//        } else {
//            return "redirect:/board_list";
//        }
//    }
//
//    // 코멘트 추가
//    @PostMapping("/articles/{articleId}/comments")
//    public String addComment(@PathVariable Long articleId, @RequestBody CommentsRequest request,
//                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        Articles article = articlesService.findJustArticle(articleId);
//        if(article != null) {
//            // 댓글 추가 로직
//            Comments comment = new Comments();
//            comment.setContent(request.getContent());
//            comment.setArticles(article); // 게시글 ID 설정
//            comment.setUsers(getLoggedInUser(customUserDetails));
//
//            commentsService.saveComment(articleId, new CommentsRequest(comment));
//
//            // 댓글 저장 (예: commentsRepository.save(comment));
//
//            // 댓글 추가 후 리다이렉션
//            return "redirect:/articles/" + articleId; // 댓글이 추가된 후 해당 게시글로 리다이렉트
//        } else {
//            // 게시글이 존재하지 않을 경우 처리
//            return "redirect:/articles"; // 게시글이 없으면 게시판 목록으로 리다이렉트
//        }
//    }
//
//    // 로그인 정보 가져오기
//    private Users getLoggedInUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        if(customUserDetails == null) {
//            return new Users();
//        }
//        return usersService.findByLoginId(customUserDetails.getUsername());
//    }
}
