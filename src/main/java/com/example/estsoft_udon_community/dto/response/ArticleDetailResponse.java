package com.example.estsoft_udon_community.dto.response;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Hashtag;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import com.example.estsoft_udon_community.util.DateFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailResponse {
    private Long id;
    private Long userId;
    private String nickname;
    private String title;
    private String content;
    private ArticleCategory category;
    private List<String> hashtags;
    private Long viewCount;
    private String createdAt;
    private String updatedAt;
    private String location;
    private Long likeCount;
    private Long commentCount;

    public ArticleDetailResponse(Articles article, Long likeCount, Long commentCount) {
        this.id = article.getId();
        this.userId = article.getUserId().getId();
        this.nickname = article.getUserId().getNickname();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.category = article.getCategory();
        this.viewCount = article.getViewCount();
        this.createdAt = article.getCreatedAt().format(DateFormatUtil.formatter);
        this.updatedAt = article.getUpdatedAt().format(DateFormatUtil.formatter);
        this.location = article.getLocation().getUpperLocation() + " " + article.getLocation().getName();
        this.hashtags = article.getHashtags().stream()
                .map(Hashtag::getName)
                .toList();
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
