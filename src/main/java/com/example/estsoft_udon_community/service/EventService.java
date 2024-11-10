package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.dto.request.EventRequest;
import com.example.estsoft_udon_community.repository.EventRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final LocationService locationService;

    // 캘린더 메인
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // 캘린더 추가
    @Transactional
    public void addEvent(EventRequest eventRequest) {
        // 작성자 ID가 null이면 예외 발생
        if (eventRequest.getUsersId() == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }

        // 작성자 찾기
        Users users = usersRepository.findById(eventRequest.getUsersId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // Location 정보를 넣어야 함
        Location location = users.getLocation();
        if (location == null) {
            throw new IllegalArgumentException("이 사용자의 위치 정보가 없습니다.");
        }

        Event event = eventRequest.toEvent(location);
        event.setUsers(users);

        eventRepository.save(event);
    }

    // 캘린더 수정
    @Transactional
    public EventResponse updateEvent(Long eventId, EventRequest eventRequest) {
        if (eventId == null) {
            throw new IllegalArgumentException("이벤트 ID는 필수입니다.");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다."));

        // 이벤트 정보 업데이트
        eventRequest.updateEvent(event);
        Event updatedEvent = eventRepository.save(event);
        return new EventResponse(updatedEvent);
    }

    // 캘린더 삭제
    @Transactional
    public void deleteEvent(Long eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("이벤트 ID는 필수입니다.");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트를 찾을 수 없습니다."));

        eventRepository.delete(event);
    }

    // 승인된 이벤트만 조회
    public List<EventResponse> getAcceptedEvents() {
        List<Event> acceptedEvents = eventRepository.findByIsAcceptedTrue();
        return acceptedEvents.stream()
                .map(EventResponse::new)
                .collect(Collectors.toList());
    }

    // 승인되지 않은 이벤트만 조회
    public List<EventResponse> getPendingEvents() {
        return eventRepository.findByIsAcceptedFalse().stream()
                .map(EventResponse::new)
                .collect(Collectors.toList());
    }
}