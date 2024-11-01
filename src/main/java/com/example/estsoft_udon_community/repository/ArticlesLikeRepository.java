package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticlesLikeRepository extends JpaRepository<ArticlesLike, Long> {
    Optional<ArticlesLike> findByArticlesAndUsers(Articles article, Users user);
}
