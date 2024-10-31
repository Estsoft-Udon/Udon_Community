package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.entity.request.EventRequest;
import com.example.estsoft_udon_community.repository.EventRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;

    // 캘린더 메인
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // 캘린더 추가
    public Event addEvent(EventRequest eventRequest){
        Event event = new Event();
        event.setTitle(eventRequest.getTitle());
        event.setDateTime(eventRequest.getDateTime());
        event.setContent(eventRequest.getContent());
        event.setRequestedAt(LocalDateTime.now());
        event.setEventType(eventRequest.getEventType()); // 축제 종류

        // 작성자 설정(임시) pk
        Users users = usersRepository.findById(eventRequest.getUserId())
                 .orElseThrow(() -> new IllegalArgumentException("User not found"));
         event.setUsers(users);

        return eventRepository.save(event);
    }

    // 캘린더 수정
    public Event updateEvent(Long eventId, EventRequest eventRequest){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not fount"));
        // 추후에  updateEvent()
        event.setTitle(eventRequest.getTitle());
        event.setDateTime(eventRequest.getDateTime());
        event.setContent(eventRequest.getContent());
        event.setRequestedAt(LocalDateTime.now());
        event.setEventType(eventRequest.getEventType()); // 축제 종류

        return eventRepository.save(event);
    }


    // 캘린더 삭제
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(event);
    }
}
