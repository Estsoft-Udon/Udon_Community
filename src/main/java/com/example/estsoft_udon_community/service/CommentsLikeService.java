package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.ArticlesLike;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.CommentsLike;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.repository.CommentsLikeRepository;
import com.example.estsoft_udon_community.repository.CommentsRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentsLikeService {

    private final CommentsLikeRepository commentsLikeRepository;
    private final UsersRepository usersRepository;
    private final CommentsRepository commentsRepository;


    public CommentsLikeService(CommentsLikeRepository commentsLikeRepository, UsersRepository usersRepository, CommentsRepository commentsRepository) {
        this.commentsLikeRepository = commentsLikeRepository;
        this.usersRepository = usersRepository;
        this.commentsRepository = commentsRepository;
    }

    public CommentsLike saveCommentsLike(Long commentsId, Long userId) {
        Comments comment = commentsRepository.findById(commentsId)
                .orElseThrow(() -> new IllegalArgumentException("comments id: "+ commentsId + "not found"));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("users id: "+ userId + "not found"));

        return commentsLikeRepository.save(new CommentsLike(comment, user));
    }

    public void deleteCommentsLike(Long commentsLikeId) {
        commentsLikeRepository.deleteById(commentsLikeId);
    }
}
