package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.*;
import com.example.estsoft_udon_community.repository.ArticlesLikeRepository;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class ArticlesLikeService {
    private final ArticlesLikeRepository articlesLikeRepository;
    private final UsersRepository usersRepository;
    private final ArticlesRepository articlesRepository;

    public ArticlesLikeService(ArticlesLikeRepository articlesLikeRepository, UsersRepository usersRepository, ArticlesRepository articlesRepository) {
        this.articlesLikeRepository = articlesLikeRepository;
        this.usersRepository = usersRepository;
        this.articlesRepository = articlesRepository;
    }

    public ArticlesLike saveArticlesLike(Long articleId, Long userId) {
        Articles articles = articlesRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("articles id: "+ articleId + "not found"));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("users id: "+ userId + "not found"));

        return articlesLikeRepository.save(new ArticlesLike(articles, user));
    }
    public void deleteArticlesLike(Long articlesLikeId) {
        articlesLikeRepository.deleteById(articlesLikeId);
    }
}
