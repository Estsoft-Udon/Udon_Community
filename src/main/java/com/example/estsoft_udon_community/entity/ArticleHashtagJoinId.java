package com.example.estsoft_udon_community.entity;

import java.io.Serializable;
import java.util.Objects;

public class ArticleHashtagJoinId implements Serializable {
    private Long articleId;
    private Long hashtagId;

    // 기본 생성자
    public ArticleHashtagJoinId() {}

    // equals 메서드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleHashtagJoinId)) return false;
        ArticleHashtagJoinId that = (ArticleHashtagJoinId) o;
        return Objects.equals(articleId, that.articleId) &&
                Objects.equals(hashtagId, that.hashtagId);
    }

    // hashCode 메서드
    @Override
    public int hashCode() {
        return Objects.hash(articleId, hashtagId);
    }
}
