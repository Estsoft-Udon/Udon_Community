package com.example.estsoft_udon_community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    //@CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //@LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Articles articles;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    public Comments(Articles articles, Users users, String content) {
        this.articles = articles;
        this.users = users;
        this.content = content;
    }

    public void updateCommentsContent(String content) {
        if(!content.isBlank()) {
            this.content = content;
        }
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
