package com.example.estsoft_udon_community.dto.response;

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
public class CommentsWithLikeResponse {
    private String content;
    private LocalDateTime createdAt;
    private Long likeCount;


    public CommentsWithLikeResponse(Comments comments, Long likeCount) {
        this.content = comments.getContent();
        this.createdAt = comments.getCreatedAt();
        this.likeCount = likeCount;
    }
}
