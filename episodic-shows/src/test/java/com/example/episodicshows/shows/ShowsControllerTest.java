package com.example.episodicshows.shows;

import com.example.episodicshows.episodes.Episode;
import com.example.episodicshows.episodes.EpisodeRepository;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ShowsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ShowRepository showRepository;

    @Autowired
    EpisodeRepository episodeRepository;

    @Test
    @Transactional
    @Rollback
    public void testGetShows() throws Exception {
        createAndSaveShow("Friends");

        MockHttpServletRequestBuilder request = get("/shows")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", equalTo("Friends")));
    }

    @Test
    @Transactional
    @Rollback
    public void testPostShows() throws Exception {
        assertThat(showRepository.count(), equalTo(0L));

        JsonObject payload = new JsonObject();
        payload.addProperty("name", "Friends");

        MockHttpServletRequestBuilder request = post("/shows")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(payload.toString());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo("Friends")));

        assertThat(showRepository.count(), equalTo(1L));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetEpisodes() throws Exception {
        Show show = createAndSaveShow("Game Of Thrones");

        Episode episode1 = getEpisode(show.getId(), 3, 2);
        Episode episode2 = getEpisode(show.getId(), 3, 1);
        episodeRepository.save(episode1);
        episodeRepository.save(episode2);

        MockHttpServletRequestBuilder request = get(
                String.format("/shows/%d/episodes", show.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(2)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].seasonNumber", equalTo(2)))
                .andExpect(jsonPath("$[0].episodeNumber", equalTo(3)))
                .andExpect(jsonPath("$[0].title", equalTo("S2 E3")))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].seasonNumber", equalTo(1)))
                .andExpect(jsonPath("$[1].episodeNumber", equalTo(3)))
                .andExpect(jsonPath("$[1].title", equalTo("S1 E3")));
    }

    @Test
    @Transactional
    @Rollback
    public void testPostEpisodes() throws Exception {
        Show show = createAndSaveShow("Parks and Recreation");

        JsonObject payload = new JsonObject();
        payload.addProperty("seasonNumber", 1);
        payload.addProperty("episodeNumber", 2);

        MockHttpServletRequestBuilder request = post(
                String.format("/shows/%s/episodes", show.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(payload.toString());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.seasonNumber", equalTo(1)))
                .andExpect(jsonPath("$.episodeNumber", equalTo(2)))
                .andExpect(jsonPath("$.title", equalTo("S1 E2")));

        List<Episode> actual = episodeRepository.findAllByShowId(show.getId());

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getSeasonNumber(), equalTo(1));
        assertThat(actual.get(0).getEpisodeNumber(), equalTo(2));
    }

    private Show createAndSaveShow(String name) {
        Show show = new Show();
        show.setName(name);
        showRepository.save(show);
        return show;
    }

    private Episode getEpisode(Long showId, int episodeNumber, int seasonNumber) {
        Episode episode = new Episode();
        episode.setEpisodeNumber(episodeNumber);
        episode.setSeasonNumber(seasonNumber);
        episode.setShowId(showId);

        return episode;
    }
}