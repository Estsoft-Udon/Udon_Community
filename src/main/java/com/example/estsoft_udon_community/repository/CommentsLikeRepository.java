package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.CommentsLike;
import com.example.estsoft_udon_community.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentsLikeRepository extends JpaRepository<CommentsLike, Long> {
    Optional<CommentsLike> findByCommentsAndUsers(Comments comments, Users user);

    @Query("SELECT c FROM Comments c " +
            "LEFT JOIN CommentsLike cl ON c = cl.comments " +
            "GROUP BY c " +
            "ORDER BY COUNT(cl.id) DESC")
    List<Comments> findCommentsOrderByLikesCountDesc();

    @Query("SELECT c, COUNT(cl.id) FROM Comments c " +
            "JOIN c.articles a " +
            "LEFT JOIN CommentsLike cl ON c = cl.comments " +
            "WHERE a.id = :articleId " +
            "GROUP BY c " +
            "ORDER BY COUNT(cl.id) DESC")
    List<Object[]> findCommentsByArticleIdOrderByLikesCountDesc(@Param("articleId") Long articleId);
}
