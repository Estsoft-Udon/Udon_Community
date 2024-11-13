package com.example.estsoft_udon_community.dto.response;

import com.example.estsoft_udon_community.entity.Comments;
import com.example.estsoft_udon_community.util.DateFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsWithLikeResponse {
    private String content;
    private String createdAt;
    private Long likeCount;


    public CommentsWithLikeResponse(Comments comments, Long likeCount) {
        this.content = comments.getContent();
        this.createdAt = comments.getCreatedAt().format(DateFormatUtil.formatter);
        this.likeCount = likeCount;
    }
}
