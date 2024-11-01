package com.example.estsoft_udon_community.dto.request;

import com.example.estsoft_udon_community.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsRequest {
    private long user_id;
    private String content;
}
