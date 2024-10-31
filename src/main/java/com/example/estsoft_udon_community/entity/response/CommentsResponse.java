package com.example.estsoft_udon_community.entity.response;

import com.example.estsoft_udon_community.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsResponse {
    private String content;
    private LocalDateTime createdAt;

    private long articleId;
    private long userId;

    public CommentsResponse(Comments comments) {
        this.content = comments.getContent();
        this.createdAt = comments.getCreatedAt();

        this.articleId = comments.getArticles().getId();
        this.userId = comments.getArticles().getUserId().getId();
    }
}
