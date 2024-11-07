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

    // 해시태그로 게시글 조회
    @Query("SELECT a FROM Articles a JOIN a.hashtags h WHERE h.id = :hashtagId AND a.isDeleted = false")
    Page<Articles> findArticlesByHashtagIdAndIsDeletedFalse(@Param("hashtagId") Long hashtagId, Pageable pageable);

    // 해시태그로 게시글 좋아요 수 정렬
    @Query("SELECT a FROM Articles a " +
            "JOIN a.hashtags h " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE h.id = :hashtagId AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findArticlesByHashtagIdOrderByLikeCount(@Param("hashtagId") Long hashtagId, Pageable pageable);

    // 해시태그로 게시글 댓글 수 정렬
    @Query("SELECT a FROM Articles a " +
            "JOIN a.hashtags h " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE h.id = :hashtagId AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findArticlesByHashtagIdOrderByCommentCount(@Param("hashtagId") Long hashtagId, Pageable pageable);

    // 많이 사용되는 해시태그 조회
    @Query("SELECT h, COUNT(ahj) as usageCount " +
            "FROM Hashtag h JOIN ArticleHashtagJoin ahj ON h.id = ahj.hashtag.id " +
            "JOIN Articles a ON ahj.articles.id = a.id " +
            "WHERE a.isDeleted = false " +
            "GROUP BY h.id " +
            "ORDER BY usageCount DESC")
    List<Object[]> findTopUsedHashtags(PageRequest pageRequest);
}
