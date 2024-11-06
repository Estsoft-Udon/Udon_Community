package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.dto.response.EventResponse;
import com.example.estsoft_udon_community.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    // 승인된 이벤트만 조회
    List<Event> findByIsAcceptedTrue();

    // 승인되지 않은 이벤트만 조회
    List<Event> findByIsAcceptedFalse();
}
