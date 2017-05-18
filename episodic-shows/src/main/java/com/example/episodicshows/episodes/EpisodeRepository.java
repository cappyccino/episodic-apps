package com.example.episodicshows.episodes;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EpisodeRepository extends CrudRepository<Episode, Long> {
    public List<Episode> findAllByShowId(Long id);
}
