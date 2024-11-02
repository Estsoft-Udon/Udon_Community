package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.ArticleHashtagJoin;
import com.example.estsoft_udon_community.entity.ArticleHashtagJoinId;
import com.example.estsoft_udon_community.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleHashtagJoinRepository extends JpaRepository<ArticleHashtagJoin, ArticleHashtagJoinId> {
    void deleteByHashtag(Hashtag hashtag);
}
