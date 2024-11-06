package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.service.admin.AdminEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    private final AdminEventService adminEventService;

    @Autowired
    public AdminViewController(AdminEventService adminEventService) {
        this.adminEventService = adminEventService;
    }

    // 이벤트 목록 조회 (기본적으로 전체 목록)
    @GetMapping("/event/event_list")
    public String eventList(@RequestParam(value = "approved", required = false) Boolean approved, Model model) {
        List<EventResponse> events;

        if (approved == null) {
            events = adminEventService.getAllEvents();
        } else if (approved) {
            events = adminEventService.getApprovedEvents();
        } else {
            events = adminEventService.getUnapprovedEvents();
        }

        model.addAttribute("events", events);
        model.addAttribute("approved", approved);
        return "admin/event/event_list";
    }


    // 이벤트 승인 여부 수정
    @GetMapping("/event/event_edit/{id}")
    public String eventEdit(@PathVariable Long id, Model model) {
        EventResponse event = adminEventService.getEventById(id);
        model.addAttribute("event", event);
        return "admin/event/event_edit";
    }

    // 이벤트 승인 처리
    @PostMapping("/event/{id}/approve")
    public String approveEvent(@PathVariable Long id) {
        adminEventService.approveEvent(id);  // 승인 처리
        return "redirect:/admin/event/event_list";  // 목록 페이지로 리다이렉트
    }

    // 이벤트 취소 처리 (승인 취소)
    @PostMapping("/event/{id}/cancel")
    public String cancelEvent(@PathVariable Long id) {
        adminEventService.cancelEvent(id);  // 승인 취소 처리
        return "redirect:/admin/event/event_list";  // 목록 페이지로 리다이렉트
    }

}
