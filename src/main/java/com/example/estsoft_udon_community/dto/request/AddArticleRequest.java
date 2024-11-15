package com.example.estsoft_udon_community.dto.request;

import com.example.estsoft_udon_community.enums.ArticleCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AddArticleRequest {
    private Long userId;
    private ArticleCategory category;
    private String title;
    private List<String> hashtagName;
    private String content;
    private Long locationId;
}