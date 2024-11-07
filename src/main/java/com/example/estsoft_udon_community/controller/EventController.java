package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.dto.request.EventRequest;
import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.repository.EventRepository;
import com.example.estsoft_udon_community.service.EventService;
import com.example.estsoft_udon_community.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final EventRepository eventRepository;

    // 캘린더 메인(이벤트 전체 조회)
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents().stream()
                .map(EventResponse::new).toList());
    }

    // 승인된 이벤트 조회
    @GetMapping("/accepted")
    public ResponseEntity<List<EventResponse>> getAcceptedEvents() {
        List<EventResponse> acceptedEvents = eventService.getAcceptedEvents();
        if (acceptedEvents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(acceptedEvents);
    }

    // 승인되지 않은 이벤트 조회
    @GetMapping("/pending")
    public List<EventResponse> getPendingEvents() {
        return eventService.getPendingEvents();
    }

    // 캘린더 추가
    @PostMapping
    public ResponseEntity<String> addEvent(@ModelAttribute EventRequest eventRequest) {
        Users loggedInUser = SecurityUtil.getLoggedInUser();
        if(loggedInUser != null) {
            eventRequest.setUsersId(loggedInUser.getId());
            EventResponse createEvent = eventService.addEvent(eventRequest);
        } else {
            return ResponseEntity.ok("등록 요청 실패~!"); // 성공 메시지 반환
        }
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
