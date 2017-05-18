package com.example.episodicshows.shows;

import com.example.episodicshows.episodes.Episode;
import com.example.episodicshows.episodes.EpisodeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowsController {

    private final ShowRepository showRepository;
    private final EpisodeRepository episodeRepository;

    public ShowsController(ShowRepository showRepository, EpisodeRepository episodeRepository) {
        this.showRepository = showRepository;
        this.episodeRepository = episodeRepository;
    }

    @GetMapping
    public Iterable<Show> getShows() {
        return showRepository.findAll();
    }

    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return showRepository.save(show);
    }

    @GetMapping("/{id}/episodes")
    public List<Episode> getEpisodes(@PathVariable("id") Long id) {
        return episodeRepository.findAllByShowId(id);
    }

    @PostMapping("/{id}/episodes")
    public Episode createEpisode(@PathVariable("id") Long id,
                                 @RequestBody() HashMap<String, Integer> map) {
        Episode episode = new Episode();
        episode.setShowId(id);
        episode.setSeasonNumber(map.get("seasonNumber"));
        episode.setEpisodeNumber(map.get("episodeNumber"));

        return episodeRepository.save(episode);
    }
}
