package com.example.episodicshows.shows;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shows")
public class ShowsController {

    private final ShowRepository showRepository;

    public ShowsController(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    @GetMapping
    public Iterable<Show> getShows() {
        return showRepository.findAll();
    }

    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return showRepository.save(show);
    }
}
