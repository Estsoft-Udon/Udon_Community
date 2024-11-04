package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.service.EventService;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    private final EventService eventService;

    public MainController(EventService eventService) {
        this.eventService = eventService;
    }
}
