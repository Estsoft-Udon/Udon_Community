package com.example.estsoft_udon_community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "articles_hashtags")
@IdClass(ArticleHashtagJoinId.class)
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
