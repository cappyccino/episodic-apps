package com.example.episodicshows.rabbit;

import lombok.Data;

import java.util.Date;

@Data
public class ProgressMessage {
    private final Long userId;
    private final Long episodeId;
    private final Date createdAt;
    private final Integer offset;
}
