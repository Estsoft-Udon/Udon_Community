package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.ArticleHashtagJoin;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Hashtag;
import com.example.estsoft_udon_community.repository.ArticleHashtagJoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleHashtagService {
    private final ArticleHashtagJoinRepository articleHashtagJoinRepository;

    // 특정 articleId에 해당하는 해시태그들을 가져오는 메서드
    public List<Hashtag> getHashtagsByArticleId(Long articleId) {
        List<ArticleHashtagJoin> articleHashtagJoins = articleHashtagJoinRepository.findByArticles_Id(articleId);

        // ArticleHashtagJoin에서 Hashtag만 추출하여 반환
        return articleHashtagJoins.stream()
                .map(ArticleHashtagJoin::getHashtag)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteByArticlesAndHashtag(Articles article, Hashtag hashtag){
        articleHashtagJoinRepository.deleteByArticlesAndHashtag(article, hashtag);
    }
}
