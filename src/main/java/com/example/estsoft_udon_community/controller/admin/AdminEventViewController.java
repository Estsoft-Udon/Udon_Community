package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.service.admin.AdminEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/event")
public class AdminEventViewController {

    private final AdminEventService adminEventService;

    @Autowired
    public AdminEventViewController(AdminEventService adminEventService) {
        this.adminEventService = adminEventService;
    }

    // 이벤트 목록 조회 (기본적으로 전체 목록)
    @GetMapping("/event_list")
    public String eventList(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(value = "approved", required = false) Boolean approved,
                            Model model) {
        Page<EventResponse> events;

        try {
            if (approved == null) {
                events = adminEventService.getAllEvents(page, size);
            } else if (approved) {
                events = adminEventService.getApprovedEvents(page, size);
            } else {
                events = adminEventService.getUnapprovedEvents(page, size);
            }

            int startIndex = page * size + 1;
            model.addAttribute("events", events);
            model.addAttribute("approved", approved);
            model.addAttribute("startIndex", startIndex);
        } catch (Exception e) {
            // 예외 처리: 로그 기록 및 사용자에게 오류 메시지 전달
            model.addAttribute("error", "이벤트 목록을 불러오는 데 실패했습니다.");
            return "admin/event/error";
        }

        return "admin/event/event_list";
    }

    // 이벤트 승인 여부 수정
    @GetMapping("/event_edit/{id}")
    public String eventEdit(@PathVariable Long id, Model model) {
        EventResponse event;
        try {
            event = adminEventService.getEventById(id);
            model.addAttribute("event", event);
        } catch (Exception e) {
            model.addAttribute("error", "이벤트를 불러오는 데 실패했습니다.");
            return "admin/event/error";
        }

        return "admin/event/event_edit";
    }

    // 이벤트 승인 처리
    @PostMapping("/{id}/approve")
    public String approveEvent(@PathVariable Long id) {
        try {
            adminEventService.approveEvent(id);
        } catch (Exception e) {
            return "redirect:/admin/event/event_list?error=승인 처리에 실패했습니다.";
        }
        return "redirect:/admin/event/event_edit/{id}";
    }

    // 이벤트 승인 취소
    @PostMapping("/{id}/cancel")
    public String cancelEvent(@PathVariable Long id) {
        try {
            adminEventService.cancelEvent(id);
        } catch (Exception e) {
            return "redirect:/admin/event/event_list?error=승인 취소 처리에 실패했습니다.";
        }
        return "redirect:/admin/event/event_edit/{id}";
    }

    // 이벤트 요청 삭제
    @PostMapping("/{id}")
    public String deleteEvent(@PathVariable Long id) {
        try {
            adminEventService.deleteEvent(id);
        } catch (Exception e) {
            return "redirect:/admin/event/event_list?error=이벤트 삭제에 실패했습니다.";
        }
        return "redirect:/admin/event/event_list";
    }
}