package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    // 삭제되지 않은 모든 게시글 조회 (페이지네이션 추가)
    Page<Articles> findByIsDeletedFalse(Pageable pageable);

    // 좋아요 수로 정렬된 전체 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findAllOrderByLikeCount(Pageable pageable);

    // 댓글 수로 정렬된 전체 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findAllOrderByCommentCount(Pageable pageable);

    // 특정 지역에 대한 게시글 조회 (페이지네이션 추가)
    Page<Articles> findByLocationIdAndIsDeletedFalse(Long locationId, Pageable pageable);

    // 특정 지역의 게시글을 좋아요 수 기준으로 정렬하여 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE a.location.id = :locationId AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findByLocationIdOrderByLikeCount(@Param("locationId") Long locationId, Pageable pageable);

    // 특정 지역의 게시글을 댓글 수 기준으로 정렬하여 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.location.id = :locationId AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findByLocationIdOrderByCommentCount(@Param("locationId") Long locationId, Pageable pageable);

    // 게시글 ID로 조회 (삭제되지 않은 것만)
    Optional<Articles> findByIdAndIsDeletedFalse(Long id);

    // 카테고리별 게시글 조회 (페이지네이션 추가)
    @Query("SELECT a FROM Articles a WHERE a.category = :category AND a.isDeleted = false")
    Page<Articles> findByCategory(@Param("category") ArticleCategory category, Pageable pageable);

    // 카테고리별 좋아요 수 기준으로 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE a.category = :category AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findByCategoryOrderByLikeCount(@Param("category") ArticleCategory category, Pageable pageable);

    // 카테고리별 댓글 수 기준으로 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.category = :category AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findByCategoryOrderByCommentCount(@Param("category") ArticleCategory category, Pageable pageable);


    // 제목으로 게시글 검색 (페이지네이션 추가)
    Page<Articles> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // 제목검색 좋아요 수 정렬 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%')) AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findByTitleContainingIgnoreCaseOrderByLikeCount(@Param("title") String title, Pageable pageable);

    // 제목검색 댓글 수 정렬 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%')) AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findByTitleContainingIgnoreCaseOrderByCommentCount(@Param("title") String title, Pageable pageable);

    // 접속지역 맛집 좋아요순 5개 정렬
    @Query("SELECT a FROM Articles a " +
            "JOIN a.location l " +
            "WHERE a.category = :category " +
            "AND l.name = :locationName " +
            "AND a.isDeleted = false " +
            "ORDER BY (SELECT COUNT(al) FROM ArticlesLike al WHERE al.articles = a) DESC")
    List<Articles> findTop5ByCategoryAndLocationOrderByLikeCountDesc(@Param("category") ArticleCategory category, @Param("locationName") String locationName);

    // 한뚝배기 - 좋아요 수 기준으로 정렬된 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "JOIN a.location l " +
            "WHERE a.category = :category " +
            "AND l = :location " +
            "AND a.isDeleted = false " +
            "ORDER BY (SELECT COUNT(al) FROM ArticlesLike al WHERE al.articles = a) DESC")
    Page<Articles> findByCategoryAndLocationOrderByLikeCountDesc(@Param("category") ArticleCategory category,
                                                                 @Param("location") Location location,
                                                                 Pageable pageable);

    // 한뚝배기 - 댓글 수 기준으로 정렬된 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "JOIN a.location l " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.category = :category " +
            "AND l = :location " +
            "AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findByCategoryAndLocationOrderByCommentCountDesc(@Param("category") ArticleCategory category,
                                                                    @Param("location") Location location,
                                                                    Pageable pageable);

    Page<Articles> findByTitleContaining(String keyword, Pageable pageable);
}