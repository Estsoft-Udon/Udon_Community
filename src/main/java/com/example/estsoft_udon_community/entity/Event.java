package com.example.estsoft_udon_community.entity;

import com.example.estsoft_udon_community.enums.EventType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 작성자와의 관계 설정
    @JoinColumn(name = "user_id")
    private Users users; // 작성한 사용자 정보 추가

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "date_time")
    private LocalDateTime dateTime; // 이벤트 날짜

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private EventType eventType; // 이벤트 종류

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @PrePersist
    public void prePersist() {
        this.requestedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
