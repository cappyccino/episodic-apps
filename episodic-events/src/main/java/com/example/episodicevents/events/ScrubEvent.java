package com.example.episodicevents.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrubEvent extends Event {
    private ScrubData data;

    @Getter
    @Setter
    public static class ScrubData {
        private Integer startOffset;
        private Integer endOffset;
    }
}
