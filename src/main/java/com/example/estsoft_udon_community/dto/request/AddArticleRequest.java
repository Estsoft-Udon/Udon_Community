package com.example.estsoft_udon_community.dto.request;

import com.example.estsoft_udon_community.entity.Hashtag;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AddArticleRequest {
    private Users userId;
    private ArticleCategory category;
    private String title;
    private List<Hashtag> hashtagName;
    private String content;
}