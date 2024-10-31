package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.entity.request.EventRequest;
import com.example.estsoft_udon_community.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private EventService eventService;

    // 캘린더 메인(이벤트 전체 조회)
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // 캘린더 추가
    @PostMapping
    public ResponseEntity<Event> addEvent(@RequestBody EventRequest eventRequest){
        Event createEvent = eventService.addEvent(eventRequest);
        return ResponseEntity.status(201).body(createEvent);
    }

    // 캘린더 수정
    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody EventRequest eventRequest){
        Event updateEvent = eventService.updateEvent(eventId, eventRequest);
        return ResponseEntity.ok(updateEvent);
    }

    // 캘린더 삭제
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
