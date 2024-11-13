package com.example.estsoft_udon_community.dto.request;

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
    private List<String> hashtagName;
}
