package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    // 특정 지역에 대한 게시글 조회 (페이지네이션 추가)
    Page<Articles> findByLocationIdAndIsDeletedFalse(Long locationId, Pageable pageable);

    // 삭제되지 않은 모든 게시글 조회 (페이지네이션 추가)
    Page<Articles> findByIsDeletedFalse(Pageable pageable);

    // 좋아요 수로 정렬된 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findAllOrderByLikeCount(Pageable pageable);

    // 댓글 수로 정렬된 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findAllOrderByCommentCount(Pageable pageable);

    // 게시글 ID로 조회 (삭제되지 않은 것만)
    Optional<Articles> findByIdAndIsDeletedFalse(Long id);

    // 카테고리별 게시글 조회 (페이지네이션 추가)
    @Query("SELECT a FROM Articles a WHERE a.category = :category AND a.isDeleted = false")
    Page<Articles> findByCategory(@Param("category") ArticleCategory category, Pageable pageable);

    // 제목으로 게시글 검색 (페이지네이션 추가)
    Page<Articles> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}