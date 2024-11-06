package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByArticlesId(Long articleId);

    @Query("SELECT c FROM Comments c WHERE c.articles.id = :articleId AND c.isDeleted = false ORDER BY c.createdAt DESC")
    Page<Comments> findNonDeletedCommentsByArticleId(@Param("articleId") Long articleId, Pageable pageable);
}
