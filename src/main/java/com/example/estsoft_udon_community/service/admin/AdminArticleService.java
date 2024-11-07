package com.example.estsoft_udon_community.service.admin;

import com.example.estsoft_udon_community.dto.request.UpdateArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.response.UsersResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import com.example.estsoft_udon_community.service.ArticlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminArticleService {
    private final UsersRepository usersRepository;
    private final ArticlesRepository articlesRepository;
    private final ArticlesService articlesService;


    //    // 관리자 게시글 조회
    public List<ArticleResponse> getAdminArticles() {
//        List<Articles> articles = articlesRepository.findByAuthorGrade(Grade.UDON_ADMIN);
        List<Articles> articles = articlesRepository.findAll();
        return articles.stream()
                .map(ArticleResponse::new)
//                .map(article -> new ArticleResponse(article, isAdmin))
                .toList();
    }

    // 관리자 특정 게시글 조회
    public Optional<ArticleResponse> getAdminByArticleId(Long articleId,boolean isAdmin) {
        return articlesRepository.findById(articleId)
                .map(ArticleResponse::new);
//                .map(article -> new ArticleResponse(article, isAdmin));
    }

    // 관리자 게시글 수정
    @Transactional
    public Articles updateArticle(Long articleId, UpdateArticleRequest request) {
        return articlesService.updateArticle(articleId, request);
    }

}