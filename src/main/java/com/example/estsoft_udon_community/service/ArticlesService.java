package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.entity.dto.AddArticleRequest;
import com.example.estsoft_udon_community.entity.dto.ArticleResponse;
import com.example.estsoft_udon_community.entity.dto.UpdateArticleRequest;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArticlesService {
    private final ArticlesRepository articlesRepository;

    public ArticlesService(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    // 게시글 등록
    public Articles saveArticle(AddArticleRequest request, Users currentUser) {
        Articles articles = new Articles(currentUser, request.getTitle(), request.getContent(), request.getCategory(), request.getHashtagName());
        return articlesRepository.save(articles);
    }

    public List<ArticleResponse> findAll() {
        return articlesRepository.findAll().stream()
                .map(ArticleResponse::new)
                .toList();
    }

    public Optional<ArticleResponse> findByArticleId(Long id) {
        return articlesRepository.findById(id)
                .map(ArticleResponse::new);
    }

    @Transactional
    public Articles updateArticle(Long id, UpdateArticleRequest request) {
        Articles article = articlesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        article.update(request.getTitle(), request.getContent(), request.getHashtags());
        return articlesRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articlesRepository.deleteById(id);
    }
}