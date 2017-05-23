package com.example.episodicshows.viewings;

import com.example.episodicshows.episodes.Episode;
import com.example.episodicshows.episodes.EpisodeRepository;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.shows.ShowRepository;
import com.example.episodicshows.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class ViewingService {

    private final ViewingRepository viewingRepository;
    private final EpisodeRepository episodeRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;

    public ViewingService(ViewingRepository viewingRepository, EpisodeRepository episodeRepository, ShowRepository showRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        assert viewingRepository != null
                && episodeRepository != null
                && showRepository != null
                && userRepository != null;

        this.viewingRepository = viewingRepository;
        this.episodeRepository = episodeRepository;
        this.showRepository = showRepository;
    }

    public List<ViewingResponseWrapper> getRecentlyWatched(Long userId) {
        List<Viewing> viewings = viewingRepository.findAllByUserId(userId);

        List<ViewingResponseWrapper> wrappers = viewings.stream().map(viewing -> {
            Show show = showRepository.findOne(viewing.getShowId());
            Episode episode = episodeRepository.findOne(viewing.getEpisodeId());

            return new ViewingResponseWrapper(viewing, show, episode);
        }).collect(Collectors.toList());

        return wrappers;
    }

    public void createOrUpdateViewing(Long userId, ViewingWrapper viewingData) {
        Long episodeId = viewingData.getEpisodeId();

        if (invalidUserId(userId)) {
            System.out.println("***************** Invalid userId!");
            return;
        }
        if (invalidEpisodeId(episodeId)) {
            System.out.println("***************** Invalid episodeId!");
            return;
        }

        Viewing viewing = viewingRepository.findByUserIdAndEpisodeId(userId, episodeId);

        if (isNull(viewing)) {
            createViewing(userId, viewingData);
        } else {
            updateViewing(viewingData, viewing);
        }
    }

    private boolean invalidEpisodeId(Long episodeId) {
        return episodeRepository.findOne(episodeId) == null;
    }

    private boolean invalidUserId(Long userId) {
        return userRepository.findOne(userId) == null;
    }

    private void updateViewing(ViewingWrapper data, Viewing viewing) {
        viewing.setUpdatedAt(data.getUpdatedAt());
        viewing.setTimecode(data.getTimecode());

        viewingRepository.save(viewing);
    }

    private void createViewing(Long userId, ViewingWrapper data) {
        Viewing viewing = new Viewing();
        Episode episode = episodeRepository.findOne(data.getEpisodeId());

        viewing.setEpisodeId(episode.getId());
        viewing.setShowId(episode.getShowId());
        viewing.setUserId(userId);
        viewing.setUpdatedAt(data.getUpdatedAt());
        viewing.setTimecode(data.getTimecode());

        viewingRepository.save(viewing);
    }
}
