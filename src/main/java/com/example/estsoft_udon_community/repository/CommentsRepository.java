package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByArticleId(Long articleId);
}
