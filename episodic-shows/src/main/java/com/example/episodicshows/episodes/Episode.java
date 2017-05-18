package com.example.episodicshows.episodes;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "episodes")
@Getter
@Setter
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long showId;
    private Integer episodeNumber;
    private Integer seasonNumber;

    @Transient
    private String title;

    @Transient
    public String getTitle() {
        return String.format("S%d E%d", this.seasonNumber, this.episodeNumber);
    }
}
