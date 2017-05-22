package com.example.episodicshows.viewings;

import com.example.episodicshows.episodes.Episode;
import com.example.episodicshows.shows.Show;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ViewingResponseWrapper {
    private Show show;
    private Episode episode;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date updatedAt;
    private Integer timecode;

    public ViewingResponseWrapper(Viewing viewing, Show show, Episode episode) {
        this.updatedAt = viewing.getUpdatedAt();
        this.timecode = viewing.getTimecode();
        this.show = show;
        this.episode = episode;
    }
}
