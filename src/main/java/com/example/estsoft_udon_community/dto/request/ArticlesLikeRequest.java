package com.example.estsoft_udon_community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticlesLikeRequest {
    private Long articleId;
    private Long userId;
}
