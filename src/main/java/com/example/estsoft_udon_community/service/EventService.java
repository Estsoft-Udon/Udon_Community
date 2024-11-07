package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.dto.request.EventRequest;
import com.example.estsoft_udon_community.repository.EventRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public EventResponse addEvent(EventRequest eventRequest) {
        // 작성자
        Users users = usersRepository.findById(eventRequest.getUsersId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Location 정보를 넣어야 함
        Event event = eventRequest.toEvent(users.getLocation());
        event.setUsers(users);

        Event saveEvent = eventRepository.save(event);
        return new EventResponse(saveEvent);
    }

    // 캘린더 수정
    public EventResponse updateEvent(Long eventId, EventRequest eventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        eventRequest.updateEvent(event);

        Event updateEvent = eventRepository.save(event);
        return new EventResponse(updateEvent);
    }

    // 캘린더 삭제
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(event);
    }

    // 승인된 이벤트만 조회
    public List<EventResponse> getAcceptedEvents() {
        List<Event> acceptedEvents = eventRepository.findByIsAcceptedTrue();  // 승인된 이벤트 조회
        return acceptedEvents.stream()
                .map(event -> new EventResponse(event))  // EventResponse로 변환
                .collect(Collectors.toList());
    }

    // 승인되지 않은 이벤트만 조회
    public List<EventResponse> getPendingEvents() {
        return eventRepository.findByIsAcceptedFalse().stream()
                .map(event -> new EventResponse(event)) // EventResponse로 변환
                .collect(Collectors.toList());
    }
}
