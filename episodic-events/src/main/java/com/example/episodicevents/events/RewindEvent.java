package com.example.episodicevents.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RewindEvent extends Event {
    private RewindData data;

    @Getter
    @Setter
    public static class RewindData {
        private Integer startOffset;
        private Integer endOffset;
        private Float speed;
    }
}
