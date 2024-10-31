package com.example.estsoft_udon_community.entity;

import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.enums.PasswordHint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Users {

    public Users(String loginId, String password, String name, String nickname,
                 String email, Grade grade,
                 PasswordHint passwordHint, String passwordAnswer) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.grade = grade;
        this.passwordHint = passwordHint;
        this.passwordAnswer = passwordAnswer;
    }

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

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Enumerated(EnumType.STRING)  // Enum을 문자열로 저장
    private Grade grade;

    @Enumerated(EnumType.STRING)  // Enum을 문자열로 저장
    @Column(name = "password_hint", nullable = false)
    private PasswordHint passwordHint;

    @Column(name = "password_answer", nullable = false)
    private String passwordAnswer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLoginAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

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
