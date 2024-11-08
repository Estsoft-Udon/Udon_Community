package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.request.EventRequest;
import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.service.admin.AdminEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/event")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    // 관리자용 모든 이벤트 목록 조회
    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Page<EventResponse> events = adminEventService.getAllEvents(page, size);
        return ResponseEntity.ok(events);
    }

    // 이벤트 승인
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveEvent(@PathVariable Long id) {
        adminEventService.approveEvent(id);
        return ResponseEntity.ok("이벤트가 승인되었습니다.");
    }

    // 이벤트 취소 처리 (승인 취소)
    @PostMapping("/event/{id}/cancel")
    public String cancelEvent(@PathVariable Long id) {
        adminEventService.cancelEvent(id);
        return "redirect:/admin/event/event_list";  // 목록 페이지로 리다이렉트
    }

    // 특정 이벤트 조회
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse eventResponse = adminEventService.getEventById(id);
        return ResponseEntity.ok(eventResponse);
    }

}