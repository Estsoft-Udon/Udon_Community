package com.example.estsoft_udon_community.dto.request;

import com.example.estsoft_udon_community.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsRequest {
    private long userId;
    private String content;
    private LocalDateTime createdAt;
    private long likeCount;

    public CommentsRequest(Comments comments) {
        this.userId = comments.getUsers().getId();
        this.content = comments.getContent();
        this.createdAt = comments.getCreatedAt();
    }
}
