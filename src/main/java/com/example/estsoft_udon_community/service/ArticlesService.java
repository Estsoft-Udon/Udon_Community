package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.entity.dto.AddArticleRequest;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import org.springframework.stereotype.Service;

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
}
