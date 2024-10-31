package com.example.estsoft_udon_community.entity.dto;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Hashtag;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private ArticleCategory category;
    private List<Hashtag> hashtags;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ArticleResponse(Articles article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.category = article.getCategory();
        this.hashtags = article.getHashtags();
        this.viewCount = article.getViewCount();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
    }
}