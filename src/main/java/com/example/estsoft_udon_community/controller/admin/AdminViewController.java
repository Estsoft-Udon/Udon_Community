package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.service.admin.AdminEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    private final AdminEventService adminEventService;

    @Autowired
    public AdminViewController(AdminEventService adminEventService) {
        this.adminEventService = adminEventService;
    }

    // 이벤트 목록 조회
    @GetMapping("/event/event_list")
    public String eventList(Model model) {
        List<EventResponse> events = adminEventService.getAllEvents();
        model.addAttribute("events", events);
        return "admin/event/event_list";
    }
}
