package com.example.episodicevents.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PauseEvent extends Event {
    private PauseData data;

    @Getter
    @Setter
    public static class PauseData {
        private Integer offset;
    }
}
