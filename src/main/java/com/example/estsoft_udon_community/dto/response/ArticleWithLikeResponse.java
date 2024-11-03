package com.example.estsoft_udon_community.dto.response;

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
public class ArticleWithLikeResponse {
    private Long userId;
    private String title;
    private String content;
    private ArticleCategory category;
    private List<String> hashtags;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String location;
    private Long likeCount;

    public ArticleWithLikeResponse(Articles article, Long likeCount) {
        // pk primary Key
        this.userId = article.getUserId().getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.category = article.getCategory();
        this.viewCount = article.getViewCount();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.location = article.getLocation().getName();
        // Hashtags 변환
        this.hashtags = article.getHashtags().stream()
                .map(Hashtag::getName)
                .toList();
        this.likeCount = likeCount;
    }
}