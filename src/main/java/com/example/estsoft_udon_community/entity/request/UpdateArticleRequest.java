package com.example.estsoft_udon_community.entity.request;

import com.example.estsoft_udon_community.entity.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateArticleRequest {
    private String title;
    private String content;
    private List<Hashtag> hashtags;
}
