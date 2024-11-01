package com.example.estsoft_udon_community.dto.request;

import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.entity.Location;
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

    private Boolean isAccepted; // 수락여부

    private Long usersId; // 작성한 사용자의 ID 추가

    private Long locationId;

    public Event toEvent(Location location) {
        Event event = new Event();

        event.setTitle(this.getTitle());
        event.setDateTime(this.getDateTime());
        event.setContent(this.getContent());
        event.setRequestedAt(LocalDateTime.now());
        event.setEventType(this.getEventType());
        event.setLocation(location);

        return event;
    }

    public void updateEvent(Event event) {
        event.setTitle(this.title);
        event.setDateTime(this.dateTime);
        event.setContent(this.content);
        event.setEventType(this.eventType);
        event.setRequestedAt(LocalDateTime.now());
    }
}
