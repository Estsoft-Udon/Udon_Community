package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.ArticleHashtagJoin;
import com.example.estsoft_udon_community.entity.ArticleHashtagJoinId;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleHashtagJoinRepository extends JpaRepository<ArticleHashtagJoin, ArticleHashtagJoinId> {
    List<ArticleHashtagJoin> findByArticles(Articles articles);
    List<ArticleHashtagJoin> findByArticles_Id(Long articleId);

    // 게시글 ID와 해시태그 이름으로 삭제
    @Modifying
    void deleteByArticlesAndHashtag(Articles articles, Hashtag hashtag);
}
