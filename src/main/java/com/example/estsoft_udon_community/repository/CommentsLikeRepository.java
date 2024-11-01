package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.CommentsLike;
import com.example.estsoft_udon_community.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentsLikeRepository extends JpaRepository<CommentsLike, Long> {
    Optional<CommentsLike> findByCommentsAndUsers(Comments comment, Users user);
}
