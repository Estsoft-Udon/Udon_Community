package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM Hashtag h WHERE h.id NOT IN (SELECT ah.hashtag.id FROM ArticleHashtagJoin ah)")
    void deleteUnusedHashtags();

    @Query("SELECT a FROM Articles a JOIN a.hashtags h WHERE h.id = :hashtagId AND a.isDeleted = false")
    Page<Articles> findArticlesByHashtagIdAndIsDeletedFalse(@Param("hashtagId") Long hashtagId, Pageable pageable);

    @Query("SELECT h, COUNT(ahj) as usageCount " +
            "FROM Hashtag h JOIN ArticleHashtagJoin ahj ON h.id = ahj.hashtag.id " +
            "GROUP BY h.id " +
            "ORDER BY usageCount DESC")
    List<Object[]> findTopUsedHashtags(PageRequest pageRequest);
}
