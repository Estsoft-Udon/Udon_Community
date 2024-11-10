package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.dto.request.EventRequest;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.service.EventService;
import com.example.estsoft_udon_community.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    // 캘린더 메인(이벤트 전체 조회)
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents().stream()
                .map(EventResponse::new)
                .toList();
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
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
    public ResponseEntity<List<EventResponse>> getPendingEvents() {
        List<EventResponse> pendingEvents = eventService.getPendingEvents();
        if (pendingEvents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pendingEvents);
    }

    // 캘린더 추가
    @PostMapping
    public ResponseEntity<String> addEvent(@ModelAttribute EventRequest eventRequest) {
        Users loggedInUser = SecurityUtil.getLoggedInUser();
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("등록 요청 실패: 로그인 필요.");
        }

        eventRequest.setUsersId(loggedInUser.getId());
        eventService.addEvent(eventRequest);
        return ResponseEntity.ok("등록 요청이 완료되었습니다.");
    }

    // 캘린더 수정
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long eventId,
                                                     @RequestBody EventRequest eventRequest) {
        EventResponse updatedEvent = eventService.updateEvent(eventId, eventRequest);
        return ResponseEntity.ok(updatedEvent);
    }

    // 캘린더 삭제
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    // 예외 처리 메서드
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 에러가 발생했습니다: " + ex.getMessage());
    }
}