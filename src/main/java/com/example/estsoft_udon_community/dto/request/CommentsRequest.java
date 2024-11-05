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
    private long user_id;
    private String content;
    private LocalDateTime created_at;

    public CommentsRequest(Comments comments) {
        this.user_id = comments.getUsers().getId();
        this.content = comments.getContent();
        this.created_at = comments.getCreatedAt();
    }
}
