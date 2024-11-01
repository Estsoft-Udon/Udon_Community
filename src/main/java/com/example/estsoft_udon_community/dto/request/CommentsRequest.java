package com.example.estsoft_udon_community.dto.request;

import com.example.estsoft_udon_community.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsRequest {
    private long user_id;
    private long article_id;
    private String content;

    public CommentsRequest(Comments comments) {
        this.article_id = comments.getArticles().getId();
        this.content = comments.getContent();
    }
}
