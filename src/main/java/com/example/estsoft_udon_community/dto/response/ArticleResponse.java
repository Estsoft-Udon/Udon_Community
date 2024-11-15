package com.example.estsoft_udon_community.dto.response;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Hashtag;
import com.example.estsoft_udon_community.entity.Location;
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
public class ArticleResponse {
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
    private Location location;
    private boolean isBlind;

    public ArticleResponse(Articles article) {
        // pk primary Key
        this.id = article.getId();
        this.userId = article.getUserId().getId();
        this.nickname = article.getUserId().getNickname();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.category = article.getCategory();
        this.viewCount = article.getViewCount();
        this.createdAt = article.getCreatedAt().format(DateFormatUtil.formatter);
        this.updatedAt = article.getUpdatedAt().format(DateFormatUtil.formatter);
        this.location = article.getLocation();
        // Hashtags 변환
        this.hashtags = article.getHashtags().stream()
                .map(Hashtag::getName)
                .toList();
        this.isBlind = article.isBlind();
    }
    public String getFullLocation(){
        return location.getUpperLocation() + " " + location.getName();
    }
}