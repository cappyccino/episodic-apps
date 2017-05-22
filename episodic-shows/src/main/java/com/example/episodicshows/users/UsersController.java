package com.example.episodicshows.users;

import com.example.episodicshows.episodes.Episode;
import com.example.episodicshows.episodes.EpisodeRepository;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.shows.ShowRepository;
import com.example.episodicshows.viewings.Viewing;
import com.example.episodicshows.viewings.ViewingRepository;
import com.example.episodicshows.viewings.ViewingResponseWrapper;
import com.example.episodicshows.viewings.ViewingWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserRepository userRepository;
    private final ViewingRepository viewingRepository;
    private final ShowRepository showRepository;
    private final EpisodeRepository episodeRepository;

    public UsersController(
            UserRepository repo,
            ViewingRepository viewingRepository,
            ShowRepository showRepository,
            EpisodeRepository episodeRepository) {
        assert repo != null
                && viewingRepository != null
                && showRepository != null
                && episodeRepository != null;

        this.showRepository = showRepository;
        this.episodeRepository = episodeRepository;
        this.viewingRepository = viewingRepository;
        this.userRepository = repo;
    }

    @GetMapping
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}/recently-watched")
    public List<ViewingResponseWrapper> getRecentlyWatched(@PathVariable("id") Long id) {
        List<Viewing> viewings = viewingRepository.findAllByUserId(id);

        List<ViewingResponseWrapper> wrappers = viewings.stream().map(viewing -> {
            Show show = showRepository.findOne(viewing.getShowId());
            Episode episode = episodeRepository.findOne(viewing.getEpisodeId());

            return new ViewingResponseWrapper(viewing, show, episode);
        }).collect(Collectors.toList());

        return wrappers;
    }

    @PatchMapping("/{id}/viewings")
    public void createOrUpdateViewing(
            @PathVariable("id") Long userId,
            @RequestBody ViewingWrapper data) {
        Long episodeId = data.getEpisodeId();
        Viewing viewing = viewingRepository.findByUserIdAndEpisodeId(userId, episodeId);

        if (isNull(viewing)) {
            viewing = new Viewing();
            Episode episode = episodeRepository.findOne(episodeId);

            viewing.setEpisodeId(episodeId);
            viewing.setShowId(episode.getShowId());
            viewing.setUserId(userId);
            viewing.setUpdatedAt(data.getUpdatedAt());
            viewing.setTimecode(data.getTimecode());
            viewingRepository.save(viewing);
        }

    }
}
