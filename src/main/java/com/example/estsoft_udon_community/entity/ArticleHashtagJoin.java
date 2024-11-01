package com.example.estsoft_udon_community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(ArticleHashtagJoinId.class)
public class ArticleHashtagJoin {

    @Id
    @Column(name = "article_id") // 외래 키 컬럼 지정
    private Long articleId; // Articles의 ID

    @Id
    @Column(name = "hashtag_id") // 외래 키 컬럼 지정
    private Long hashtagId; // Hashtag의 ID

    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false) // 외래 키 매핑
    private Articles articles;

    @ManyToOne
    @JoinColumn(name = "hashtag_id", insertable = false, updatable = false) // 외래 키 매핑
    private Hashtag hashtag;
}