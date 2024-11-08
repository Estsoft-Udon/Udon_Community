package com.example.estsoft_udon_community.service.admin;

import com.example.estsoft_udon_community.dto.request.UpdateArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import com.example.estsoft_udon_community.service.ArticlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminArticleService {
    private final UsersRepository usersRepository;
    private final ArticlesRepository articlesRepository;
    private final ArticlesService articlesService;


    // 관리자 게시글 조회
    public Page<ArticleResponse> getAdminArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Articles> articles = articlesRepository.findAll(pageable);
        return articles.map(ArticleResponse::new);
    }

    // 관리자 특정 게시글 조회
    public ArticleResponse getArticleById(Long id) {
        Articles article = articlesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return new ArticleResponse(article);
    }
    // 선택한 회원의 정보
//    public Articles getArticleById(Long id) {
//        return articlesRepository.findById(id).orElseThrow();
//    }

    // 관리자 게시글 수정
    @Transactional
    public Articles updateArticle(Long articleId, UpdateArticleRequest request) {
        return articlesService.updateArticle(articleId, request);
    }

}