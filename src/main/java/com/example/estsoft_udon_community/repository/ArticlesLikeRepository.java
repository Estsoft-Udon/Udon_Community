package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticlesLikeRepository extends JpaRepository<ArticlesLike, Long> {
    Optional<ArticlesLike> findByArticlesAndUsers(Articles article, Users user);

    @Query("SELECT a FROM Articles a " +
            "LEFT JOIN ArticlesLike al ON a = al.articles " +
            "GROUP BY a " +
            "ORDER BY COUNT(al.id) DESC")
    List<Articles> findArticlesOrderByLikesCountDesc();
}
