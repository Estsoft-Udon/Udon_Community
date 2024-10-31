package com.example.estsoft_udon_community.entity.response;

import com.example.estsoft_udon_community.entity.Articles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsArticlesResponse {
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentsResponse> comments = new ArrayList<>();

    public CommentsArticlesResponse(Articles articles, List<CommentsResponse> comments) {
        this.title = articles.getTitle();
        this.content = articles.getContent();
        this.createdAt = articles.getCreatedAt();
        this.updatedAt = articles.getUpdatedAt();
        this.comments = comments;
    }
}
