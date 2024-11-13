package com.example.estsoft_udon_community.dto.response;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.util.DateFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsArticlesResponse {
    private String userNickname;

    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private List<CommentsResponse> comments = new ArrayList<>();

    public CommentsArticlesResponse(Articles articles, List<CommentsResponse> comments) {
        this.userNickname = articles.getUserId().getNickname();
        this.title = articles.getTitle();
        this.content = articles.getContent();
        this.createdAt = articles.getCreatedAt().format(DateFormatUtil.formatter);
        this.updatedAt = articles.getUpdatedAt().format(DateFormatUtil.formatter);
        this.comments = comments;
    }
}
