package com.example.estsoft_udon_community.entity;

import com.example.estsoft_udon_community.enums.ArticleCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Articles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ArticleCategory category;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "articles_hashtags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private List<Hashtag> hashtags;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "is_blind", nullable = false)
    private boolean isBlind = false;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Articles (Users userId, String title, String content, ArticleCategory category, List<Hashtag> hashtags, Location location) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.hashtags = hashtags;
        this.location = location;
    }

    // 뷰카운트 증가
    public void incrementViewCount() {
        this.viewCount += 1;
    }
}