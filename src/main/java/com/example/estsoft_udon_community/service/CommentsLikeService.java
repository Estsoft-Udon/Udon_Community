package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.CommentsLike;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.repository.CommentsLikeRepository;
import com.example.estsoft_udon_community.repository.CommentsRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentsLikeService {
    private final CommentsLikeRepository commentsLikeRepository;
    private final UsersRepository usersRepository;
    private final CommentsRepository commentsRepository;
    private final CommentsService commentsService;

    public Long pressCommentsLike(Long commentsId, Long userId) {
        Comments comment = commentsRepository.findById(commentsId)
                .orElseThrow(() -> new IllegalArgumentException("comments id: " + commentsId + " not found"));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("users id: " + userId + " not found"));

        // 코멘트, 유저로 좋아요가 존재하는지 검색 후 없으면 저장, 있으면 삭제
        Optional<CommentsLike> foundLike = commentsLikeRepository.findByCommentsAndUsers(comment, user);

        if (foundLike.isPresent()) {
            // 좋아요가 이미 존재할 경우 삭제하고 null을 반환
            commentsLikeRepository.delete(foundLike.get());
            System.out.println("좋아요 삭제");
        } else {
            // 좋아요가 없을 경우 새로 저장하고 저장된 객체 반환
            CommentsLike newLike = new CommentsLike(comment, user);
            commentsLikeRepository.save(newLike);
            System.out.println("좋아요 생성");
        }
        return commentsLikeRepository.countByCommentsId(commentsId);
    }

    public List<Comments> findCommentsOrderByLikesCountDesc() {
        return commentsLikeRepository.findCommentsOrderByLikesCountDesc();
    }

    public List<Object[]> findCommentsByArticleIdOrderByLikesCountDesc(Long articleId) {
        return commentsLikeRepository.findCommentsByArticleIdOrderByLikesCountDesc(articleId);
    }

    public Long countCommentsLike(Long commentId) {
        if(commentsService.isDeleted(commentId)) {
            return 0L;
        }
        return commentsLikeRepository.countByCommentsId(commentId);
    }

    public Map<Long, Long> countCommentsLikeGroupByUser() {
        List<CommentsLike> commentsLikes = commentsLikeRepository.findAll();

        Map<Long, Long> commentsMap = new HashMap<>();      // userId, likeCount;
        for(CommentsLike commentsLike : commentsLikes) {
            Long userId = commentsLike.getUsers().getId();
            commentsMap.merge(userId, 1L, Long::sum);
        }

        System.out.println(commentsMap);

        return commentsMap;
    }
}
