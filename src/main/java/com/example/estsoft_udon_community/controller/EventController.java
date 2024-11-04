package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.dto.response.UsersResponse;
import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.dto.request.EventRequest;
import com.example.estsoft_udon_community.service.EventService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    // 캘린더 메인(이벤트 전체 조회)
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents().stream()
                .map(EventResponse::new).toList());
    }

    // 캘린더 추가
    @PostMapping
    public ResponseEntity<String> addEvent(@ModelAttribute EventRequest eventRequest) {
        EventResponse createEvent = eventService.addEvent(eventRequest);
        return ResponseEntity.ok("등록 요청이 완료되었습니다."); // 성공 메시지 반환
    }

    // 캘린더 수정
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long eventId,
                                                     @RequestBody EventRequest eventRequest) {
        EventResponse updateEvent = eventService.updateEvent(eventId, eventRequest);
        return ResponseEntity.ok(updateEvent);
    }

    // 캘린더 삭제
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
