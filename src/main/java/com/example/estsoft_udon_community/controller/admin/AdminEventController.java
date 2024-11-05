package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.request.EventRequest;
import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.service.admin.AdminEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/event")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    // 관리자용 모든 이벤트 목록 조회
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = adminEventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // 이벤트 승인
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveEvent(@PathVariable Long id) {
        adminEventService.approveEvent(id);
        return ResponseEntity.ok("이벤트가 승인되었습니다.");
    }

    // 이벤트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        adminEventService.deleteEvent(id);
        return ResponseEntity.ok("이벤트가 삭제되었습니다.");
    }

    // 특정 이벤트 조회
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse eventResponse = adminEventService.getEventById(id);
        return ResponseEntity.ok(eventResponse);
    }
}