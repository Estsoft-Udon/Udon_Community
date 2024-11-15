package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.dto.response.CommentsResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.dto.request.CommentsRequest;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import com.example.estsoft_udon_community.repository.CommentsRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import com.example.estsoft_udon_community.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final ArticlesRepository articlesRepository;
    private final UsersRepository usersRepository;

    // 댓글 추가
    public Comments saveComment(Long articleId, CommentsRequest request) {
        Long userId = request.getUserId();

        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found user id : " + userId));

        Articles articles = articlesRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("not found article id : " + articleId));

        Comments saveComment = commentsRepository.save(new Comments(articles, users, request.getContent()));

        // 자동 등업 - 댓글 한개 추가시 등업
        if (users.getGrade() == Grade.UDON) {
            users.setGrade(Grade.UDON_FRIEND);
            usersRepository.save(users);
            // 세션의 권한 정보 업데이트
            SecurityUtil.updateAuthentication(users);
        }

        return saveComment;
    }


    // 댓글 목록 조회
    public Comments findComment(Long commentsId) {
        return commentsRepository.findById(commentsId)
                .filter(comment -> !comment.getIsDeleted()) // 삭제되지 않은 댓글만 반환
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다. ID: " + commentsId));
    }

    public List<Comments> findCommentsByArticleId(Long articleId) {
        articlesRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("not found article id : " + articleId));

        return commentsRepository.findByArticlesId(articleId);
    }

    // 댓글 수정
    public Comments update(Long commentId, CommentsRequest request) {
        if (isDeleted(commentId)) {
            throw (new EntityNotFoundException("삭제된 댓글입니다. ID: " + commentId));
        }
        Comments comments = findComment(commentId);
        comments.updateCommentsContent(request.getContent());
        comments.setUpdatedAt(LocalDateTime.now());

        return commentsRepository.save(comments);
    }

    // 댓글 삭제
    public void deleteBy(Long commentsId) {
        commentsRepository.deleteById(commentsId);
    }


    // 댓글 soft delete
    public Comments softDelete(Long commentId) {
        if (isDeleted(commentId)) {
            throw (new EntityNotFoundException("삭제된 댓글입니다. ID: " + commentId));
        }
        Comments comments = findComment(commentId);
        comments.setIsDeleted(true);
        comments.setDeletedAt(LocalDateTime.now());

        return commentsRepository.save(comments);
    }


    // 댓글 삭제 여부 검사
    public boolean isDeleted(Long commentId) {
        Comments comments = commentsRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("댓글을 찾을 수 없습니다. ID: " + commentId)
        );

        if (comments.getIsDeleted()) {
            return true;
        }
        return false;
    }

    public Page<CommentsResponse> findCommentsByArticleId(Long articleId, Pageable pageable) {
        return commentsRepository.findNonDeletedCommentsByArticleId(articleId, pageable)
                .map(CommentsResponse::new);
    }
}