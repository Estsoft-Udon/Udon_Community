package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.request.EventRequest;
import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.service.admin.AdminEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/event")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    // 모든 이벤트 목록 조회
    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Page<EventResponse> events = adminEventService.getAllEvents(page, size);
        return ResponseEntity.ok(events);
    }

    // 이벤트 승인
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveEvent(@PathVariable Long id) {
        try {
            adminEventService.approveEvent(id);
            return ResponseEntity.ok("이벤트가 승인되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("이벤트를 찾을 수 없습니다.");
        }
    }

    // 이벤트 승인 취소
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelEvent(@PathVariable Long id) {
        try {
            adminEventService.cancelEvent(id);
            return ResponseEntity.ok("이벤트의 승인 취소가 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("유효하지 않은 이벤트 ID입니다.");
        }
    }

    // 이벤트 조회
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        try {
            EventResponse eventResponse = adminEventService.getEventById(id);
            return ResponseEntity.ok(eventResponse);
        } catch (IllegalArgumentException e) {
            // 이벤트를 찾을 수 없을 경우 400 Bad Request
            return ResponseEntity.badRequest().body(null);
        }
    }
}