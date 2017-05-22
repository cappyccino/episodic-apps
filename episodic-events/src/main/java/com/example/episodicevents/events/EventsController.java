package com.example.episodicevents.events;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class EventsController {

    private final EventsRepository eventsRepository;

    public EventsController(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    @GetMapping("/recent")
    public Iterable<Event> getRecentEvents() {
        return eventsRepository.findAll(
                new PageRequest(
                        0,
                        20,
                        new Sort(Sort.Direction.DESC, "createdAt"))
        ).getContent();
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventsRepository.save(event);
    }
}
