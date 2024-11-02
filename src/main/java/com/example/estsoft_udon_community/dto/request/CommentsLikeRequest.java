package com.example.estsoft_udon_community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentsLikeRequest {
    private Long commentId;
    private Long userId;
}
