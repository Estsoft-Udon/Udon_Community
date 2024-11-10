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
    // 게시글 ID로 조회 (삭제되지 않은 것만)
    Optional<Articles> findByIdAndIsDeletedFalse(Long id);

    // 삭제되지 않은 모든 게시글 조회 (페이지네이션 추가)
    Page<Articles> findByIsDeletedFalse(Pageable pageable);
    // 삭제되지 않은 모든 게시글 중 제목에 검색어가 포함된 게시글 조회 (페이지네이션 추가)
    Page<Articles> findByIsDeletedFalseAndTitleContaining(String title, Pageable pageable);
    // 좋아요 수로 정렬된 전체 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findAllOrderByLikeCount(Pageable pageable);
    // 좋아요 수로 정렬된 삭제되지 않은 제목 검색 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE a.isDeleted = false AND a.title LIKE %:title% " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findByTitleContainingOrderByLikeCount(Pageable pageable, String title);
    // 댓글 수로 정렬된 전체 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findAllOrderByCommentCount(Pageable pageable);
    // 댓글 수로 정렬된 삭제되지 않은 제목 검색 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.isDeleted = false AND a.title LIKE %:title% " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findByTitleContainingOrderByCommentCount(Pageable pageable, String title);

    // 특정 지역에 대한 게시글 조회 (페이지네이션 추가)
    Page<Articles> findByLocationIdAndIsDeletedFalse(Long locationId, Pageable pageable);
    // 특정 지역에 대한 게시글 조회 (제목 포함, 페이지네이션 추가)
    Page<Articles> findByLocationIdAndIsDeletedFalseAndTitleContaining(Long locationId, String title, Pageable pageable);
    // 특정 지역의 게시글을 좋아요 수 기준으로 정렬하여 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE a.location.id = :locationId AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findByLocationIdOrderByLikeCount(@Param("locationId") Long locationId, Pageable pageable);
    // 특정 지역의 게시글을 좋아요 수 기준으로 정렬하여 제목 포함 검색
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE a.location.id = :locationId AND a.isDeleted = false AND a.title LIKE %:title% " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findByLocationIdAndTitleContainingOrderByLikeCount(@Param("locationId") Long locationId, @Param("title") String title, Pageable pageable);
    // 특정 지역의 게시글을 댓글 수 기준으로 정렬하여 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.location.id = :locationId AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findByLocationIdOrderByCommentCount(@Param("locationId") Long locationId, Pageable pageable);
    // 특정 지역의 게시글을 댓글 수 기준으로 정렬하여 제목 포함 검색
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.location.id = :locationId AND a.isDeleted = false AND a.title LIKE %:title% " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findByLocationIdAndTitleContainingOrderByCommentCount(@Param("locationId") Long locationId, @Param("title") String title, Pageable pageable);

    // 카테고리별 게시글 조회 (페이지네이션 추가)
    @Query("SELECT a FROM Articles a WHERE a.category = :category AND a.isDeleted = false")
    Page<Articles> findByCategory(@Param("category") ArticleCategory category, Pageable pageable);
    // 카테고리별 제목 검색을 포함한 게시글 조회
    @Query("SELECT a FROM Articles a WHERE a.category = :category AND a.isDeleted = false AND a.title LIKE %:title%")
    Page<Articles> findByCategoryAndTitleContaining(@Param("category") ArticleCategory category, @Param("title") String title, Pageable pageable);
    // 카테고리별 좋아요 수 기준으로 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE a.category = :category AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findByCategoryOrderByLikeCount(@Param("category") ArticleCategory category, Pageable pageable);
    // 카테고리별 좋아요 수 기준으로 정렬된 제목 포함 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a.id = al.articles.id " +
            "WHERE a.category = :category AND a.isDeleted = false AND a.title LIKE %:title% " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(al.id) DESC")
    Page<Articles> findByCategoryAndTitleContainingOrderByLikeCount(@Param("category") ArticleCategory category, @Param("title") String title, Pageable pageable);
    // 카테고리별 댓글 수 기준으로 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.category = :category AND a.isDeleted = false " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findByCategoryOrderByCommentCount(@Param("category") ArticleCategory category, Pageable pageable);
    // 카테고리별 댓글 수 기준으로 정렬된 제목 포함 게시글 조회
    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.category = :category AND a.isDeleted = false AND a.title LIKE %:title% " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findByCategoryAndTitleContainingOrderByCommentCount(@Param("category") ArticleCategory category, @Param("title") String title, Pageable pageable);

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
    // 한뚝배기 - 좋아요 수 기준으로 정렬된 게시글 조회 (제목 검색 포함)
    @Query("SELECT a FROM Articles a " +
            "JOIN a.location l " +
            "WHERE a.category = :category " +
            "AND l = :location " +
            "AND a.isDeleted = false " +
            "AND a.title LIKE %:title% " +  // 제목 검색어 추가
            "ORDER BY (SELECT COUNT(al) FROM ArticlesLike al WHERE al.articles = a) DESC")
    Page<Articles> findByCategoryAndLocationAndTitleContainingOrderByLikeCountDesc(@Param("category") ArticleCategory category,
                                                                                   @Param("location") Location location,
                                                                                   @Param("title") String title,
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
    // 한뚝배기 - 댓글 수 기준으로 정렬된 게시글 조회 (제목 검색 포함)
    @Query("SELECT a FROM Articles a " +
            "JOIN a.location l " +
            "LEFT JOIN Comments c ON a.id = c.articles.id " +
            "WHERE a.category = :category " +
            "AND l = :location " +
            "AND a.isDeleted = false " +
            "AND a.title LIKE %:title% " +  // 제목 검색어 추가
            "GROUP BY a.id " +
            "ORDER BY COUNT(c.id) DESC")
    Page<Articles> findByCategoryAndLocationAndTitleContainingOrderByCommentCountDesc(@Param("category") ArticleCategory category,
                                                                                      @Param("location") Location location,
                                                                                      @Param("title") String title,
                                                                                      Pageable pageable);

    // 게시글 검색기능
    Page<Articles> findByTitleContaining(String keyword, Pageable pageable);

    // 제목에 키워드가 포함되고, 삭제되지 않은 게시글을 찾는 메서드
    Page<Articles> findByTitleContainingAndIsDeletedFalse(String title, Pageable pageable);

    // 공개 상태인 게시글만 반환
    List<Articles> findByIsBlind(boolean isBlind);

}