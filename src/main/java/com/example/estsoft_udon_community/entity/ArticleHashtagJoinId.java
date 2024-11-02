package com.example.estsoft_udon_community.entity;

import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
public class ArticleHashtagJoinId implements Serializable {
    private Long articles;  // Articles 엔티티의 필드명과 일치시킴
    private Long hashtag;   // Hashtag 엔티티의 필드명과 일치시킴

    // equals 메서드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleHashtagJoinId)) return false;
        ArticleHashtagJoinId that = (ArticleHashtagJoinId) o;
        return Objects.equals(articles, that.articles) &&
                Objects.equals(hashtag, that.hashtag);
    }

    // hashCode 메서드
    @Override
    public int hashCode() {
        return Objects.hash(articles, hashtag);
    }
}
