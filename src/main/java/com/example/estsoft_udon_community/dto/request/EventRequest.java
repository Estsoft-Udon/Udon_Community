package com.example.estsoft_udon_community.dto.request;

import com.example.estsoft_udon_community.enums.EventType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequest {
    private Long id;

    private LocalDateTime dateTime; // 이벤트 날짜

    private String title;

    private String content;

    private EventType eventType;

    private LocalDateTime requestedAt; // 요청시각

    private LocalDateTime updatedAt; // 수정시각

    private LocalDateTime deletedAt; // 삭제시각

    private Boolean isAccepted; // 수락여부

    private Long userId; // 작성한 사용자의 ID 추가
}
