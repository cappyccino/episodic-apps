package com.example.episodicevents.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FastForwardEvent extends Event {
    private FastForwardData data;

    @Getter
    @Setter
    public static class FastForwardData {
        private Integer startOffset;
        private Integer endOffset;
        private Float speed;
    }
}
