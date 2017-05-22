package com.example.episodicevents.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressEvent extends Event {
    private ProgressData data;

    @Getter
    @Setter
    public static class ProgressData {
        private Integer offset;
    }
}
