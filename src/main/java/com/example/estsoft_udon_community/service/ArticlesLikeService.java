package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.*;
import com.example.estsoft_udon_community.repository.ArticlesLikeRepository;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Long pressArticlesLike(Long articlesId, Long userId) {
        Articles article = articlesRepository.findById(articlesId)
                .orElseThrow(() -> new IllegalArgumentException("articles id: " + articlesId + " not found"));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("users id: " + userId + " not found"));

        // 코멘트, 유저로 좋아요가 존재하는지 검색 후 없으면 저장, 있으면 삭제
        Optional<ArticlesLike> foundLike = articlesLikeRepository.findByArticlesAndUsers(article, user);

        if (foundLike.isPresent()) {
            // 좋아요가 이미 존재할 경우 삭제하고 null을 반환
            articlesLikeRepository.delete(foundLike.get());
        } else {
            // 좋아요가 없을 경우 새로 저장하고 저장된 객체 반환
            ArticlesLike newLike = new ArticlesLike(article, user);
            articlesLikeRepository.save(newLike);
        }
        return articlesLikeRepository.countLikesByArticles(article);
    }

    public List<Object[]> findArticlesOrderByLikesCountDesc() {
        return articlesLikeRepository.findArticlesOrderByLikesCountDescWithCount();
    }

    public Long getLikeCountForArticle(Long articleId) {
        // 게시글을 찾기 위해 ArticlesRepository를 사용
        Articles article = articlesRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        // 좋아요 수 세기
        return articlesLikeRepository.countArticlesLikeByArticles(article);
    }

    public Map<Long, Long> countArticlesLikeGroupByUser() {
        List<ArticlesLike> articlesLikes = articlesLikeRepository.findAll();

        Map<Long, Long> articlesMap = new HashMap<>();      // userId, likeCount;
        for(ArticlesLike articlesLike : articlesLikes) {
            Long userId = articlesLike.getUsers().getId();
            articlesMap.merge(userId, 1L, Long::sum);
        }

        return articlesMap;
    }
}
