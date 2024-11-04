package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.ArticleHashtagJoin;
import com.example.estsoft_udon_community.entity.ArticleHashtagJoinId;
import com.example.estsoft_udon_community.entity.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleHashtagJoinRepository extends JpaRepository<ArticleHashtagJoin, ArticleHashtagJoinId> {
    List<ArticleHashtagJoin> findByArticles(Articles articles);
}
