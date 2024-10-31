package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
