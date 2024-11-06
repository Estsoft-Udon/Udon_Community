package com.example.estsoft_udon_community.dto.response;

import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsResponse {
    private long id;
    private Users user;
    private String nickName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long likeCount;

    public CommentsResponse(Comments comments) {
        this.id = comments.getId();
        this.user = comments.getUsers();
        this.nickName = comments.getUsers().getNickname();
        this.content = comments.getContent();
        this.createdAt = comments.getCreatedAt();
        this.updatedAt = comments.getUpdatedAt();
    }
}
