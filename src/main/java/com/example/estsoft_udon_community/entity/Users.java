package com.example.estsoft_udon_community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)  // Enum을 문자열로 저장
    private Grade grade;

    @Enumerated(EnumType.STRING)  // Enum을 문자열로 저장
    @Column(name = "password_hint", nullable = false)
    private PasswordHint passwordHint;

    @Column(name = "password_answer", nullable = false)
    private String passwordAnswer;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "last_login", nullable = false)
    private LocalDateTime lastLoginAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    // 연관관계 추가
    // Articles
    // Articles_like
    // comments_like

    // 연관관계 추가
    @OneToMany(mappedBy = "users")  // Articles와의 1:N 관계
    private List<Articles> articles;

//    @OneToMany(mappedBy = "user")  // Comments와의 1:N 관계
//    private List<Comment> comments;

    @OneToMany(mappedBy = "users")  // Articles_like와의 1:N 관계
    private List<ArticlesLike> articleLikes;

//    @OneToMany(mappedBy = "user")  // Comments_like와의 1:N 관계
//    private List<CommentLike> commentLikes;
//
}
