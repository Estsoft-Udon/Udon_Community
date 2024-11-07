package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Event;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    // 승인된 이벤트만 조회
    Page<Event> findByIsAcceptedTrue(Pageable pageable);

    // 승인되지 않은 이벤트만 조회
    Page<Event> findByIsAcceptedFalse(Pageable pageable);

    List<Event> findByIsAcceptedTrue();

    List<Event> findByIsAcceptedFalse();
}
