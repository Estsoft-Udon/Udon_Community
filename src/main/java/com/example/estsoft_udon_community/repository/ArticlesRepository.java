package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    List<Articles> findByLocationIdAndIsDeletedFalse(Long locationId);

    List<Articles> findByIsDeletedFalse();

    Optional<Articles> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT a FROM Articles a WHERE a.category = :category")
    List<Articles> findByCategory(@Param("category") ArticleCategory category);

    List<Articles> findByTitleContainingIgnoreCase(String title);
}