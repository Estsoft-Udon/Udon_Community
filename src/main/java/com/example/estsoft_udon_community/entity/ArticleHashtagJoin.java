package com.example.estsoft_udon_community.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ArticleHashtagJoin {
    @Id
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Articles articles;

    @Id
    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;
}
