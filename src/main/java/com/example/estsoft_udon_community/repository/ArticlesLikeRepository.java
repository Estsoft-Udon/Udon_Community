package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ArticlesLikeRepository extends JpaRepository<ArticlesLike, Long> {
    Optional<ArticlesLike> findByArticlesAndUsers(Articles article, Users user);

    @Query("SELECT a, COUNT(al.id) FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a = al.articles " +
            "GROUP BY a " +
            "ORDER BY COUNT(al.id) DESC")
    List<Object[]> findArticlesOrderByLikesCountDescWithCount();

    Long countArticlesLikeByArticles(Articles article);
    Long countLikesByArticles(Articles article);

    @Query("SELECT a.userId.id AS userId, COUNT(al) AS likeCount " +
            "FROM ArticlesLike al " +
            "JOIN al.articles a " +  // ArticlesLike와 Article을 조인
            "GROUP BY a.userId.id")  // 작성자 ID로 그룹화
    List<Map<Long, Long>> findTopUsersByArticleLikes();

}
