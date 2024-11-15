package com.example.estsoft_udon_community.dto.response;

import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.enums.EventType;
import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.util.DateFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;

    private String dateTime;

    private String title;

    private String content;

    private EventType eventType;

    private Boolean isAccepted; // 수락 여부

    private Long usersId; // 작성한 사용자의 ID

    private String userLoginId;

    private String userName;

    private String userGrade;

    private String nickName;

    public EventResponse(Event event) {
        this.id = event.getId();
        this.dateTime = event.getDateTime().format(DateFormatUtil.formatter);
        this.title = event.getTitle();
        this.content = event.getContent();
        this.eventType = event.getEventType();
        this.isAccepted = (event.getIsAccepted() != null) ? event.getIsAccepted() : false;
        this.usersId = event.getUsers().getId();
        this.userLoginId = event.getUsers().getLoginId();
        this.userName = event.getUsers().getName();
        this.userGrade = event.getUsers().getGrade().name();
        this.nickName = event.getUsers().getNickname();
    }
}
