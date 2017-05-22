package com.example.episodicshows.viewings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ViewingWrapper {
    private Long episodeId;
    private Date updatedAt;
    private Integer timecode;
}
