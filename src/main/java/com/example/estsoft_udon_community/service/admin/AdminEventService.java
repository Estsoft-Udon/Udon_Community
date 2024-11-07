package com.example.estsoft_udon_community.service.admin;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository eventRepository;

    // 모든 이벤트 조회 (승인 여부 상관없이)
    public Page<EventResponse> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> allEvent = eventRepository.findAll(pageable);
        return allEvent.map(EventResponse::new);
    }

    // 승인된 이벤트 목록 조회
    public Page<EventResponse> getApprovedEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> byIsAcceptedTrue = eventRepository.findByIsAcceptedTrue(pageable);
        return byIsAcceptedTrue.map(EventResponse::new); // EventResponse로 변환
    }

    // 승인되지 않은 이벤트 목록 조회
    public Page<EventResponse> getUnapprovedEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> byIsAcceptedFalse = eventRepository.findByIsAcceptedFalse(pageable);
        return byIsAcceptedFalse.map(EventResponse::new);
    }

    // 이벤트 승인
    @Transactional
    public void approveEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        event.setAccepted(true);
        eventRepository.save(event);
    }

    // 이벤트 승인 취소 처리
    public void cancelEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));
        event.setIsAccepted(false);  // 승인 취소 처리
        eventRepository.save(event);
    }

    // 특정 이벤트 상세 조회
    public EventResponse getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return new EventResponse(event);
    }

    // 이벤트 삭제 메서드 추가
    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(event);
    }
}