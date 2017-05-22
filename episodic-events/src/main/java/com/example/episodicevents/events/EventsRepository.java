package com.example.episodicevents.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface EventsRepository extends MongoRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);
}
