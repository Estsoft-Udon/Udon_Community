package com.example.estsoft_udon_community.service.admin;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.entity.Event;
import com.example.estsoft_udon_community.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository eventRepository;

    // 모든 이벤트 조회 (승인 여부 상관없이)
    public Page<EventResponse> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> allEvent = eventRepository.findAllByOrderByRequestedAtDesc(pageable);
        return allEvent.map(EventResponse::new);
    }

    // 승인된 이벤트 목록 조회
    public Page<EventResponse> getApprovedEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> byIsAcceptedTrue = eventRepository.findByIsAcceptedTrue(pageable);
        return byIsAcceptedTrue.map(EventResponse::new);
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
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));
        event.setAccepted(true);
        eventRepository.save(event);
    }

    // 이벤트 승인 취소 처리
    @Transactional
    public void cancelEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 이벤트 ID입니다."));
        event.setIsAccepted(false);
        eventRepository.save(event);
    }

    // 특정 이벤트 상세 조회
    public EventResponse getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));
        return new EventResponse(event);
    }

    // 이벤트 삭제
    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));
        eventRepository.delete(event);
    }
}