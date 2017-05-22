package com.example.episodicevents.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayEvent extends Event {
    private PlayData data;

    @Getter
    @Setter
    public static class PlayData {
        private Integer offset;
    }
}
