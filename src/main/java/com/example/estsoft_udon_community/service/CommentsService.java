package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.dto.request.CommentsRequest;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import com.example.estsoft_udon_community.repository.CommentsRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final ArticlesRepository articlesRepository;
    private final UsersRepository usersRepository;

    // 댓글 추가
    public Comments saveComment(Long articleId, CommentsRequest request) {
        Articles articles = articlesRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("not found article id : " + articleId));

        Users users = usersRepository.findById(request.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("not found user id : " + request.getUser_id()));

        return commentsRepository.save(new Comments(articles, users, request.getContent()));
    }

    // 댓글 목록 조회
    public Comments findComment(Long commentsId) {
        Optional<Comments> optionalComments = commentsRepository.findById(commentsId);
        return optionalComments.orElse(new Comments());
    }

    public List<Comments> findCommentsByArticleId(Long articleId) {
        articlesRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("not found article id : " + articleId));

        return commentsRepository.findByArticlesId(articleId);
    }

    // 댓글 수정
    public Comments update(Long commentId, CommentsRequest request) {
        Comments comments = findComment(commentId);
        comments.updateCommentsContent(request.getContent());

        return commentsRepository.save(comments);
    }

    // 댓글 삭제
    public void deleteBy(Long commentsId) {
        commentsRepository.deleteById(commentsId);
    }
}

/*
댓글 추가	POST	/api/articles/{articleId}/comments
댓글목록조회	GET	/api/articles/{articleId}/comments
댓글 수정	PUT	/api/comments/{commentId}
댓글 삭제	DELETE	/api/comments/{commentId}
 */