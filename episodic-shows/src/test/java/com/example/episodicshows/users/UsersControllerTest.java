package com.example.episodicshows.users;

import com.example.episodicshows.TestBase;
import com.example.episodicshows.episodes.Episode;
import com.example.episodicshows.episodes.EpisodeRepository;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.shows.ShowRepository;
import com.example.episodicshows.viewings.Viewing;
import com.example.episodicshows.viewings.ViewingRepository;
import com.example.episodicshows.viewings.ViewingService;
import com.example.episodicshows.viewings.ViewingWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(secure = false)
public class UsersControllerTest extends TestBase {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ShowRepository showRepository;

    @Autowired
    EpisodeRepository episodeRepository;

    @Autowired
    ViewingRepository viewingRepository;

    @Autowired
    ViewingService viewingService;

    @Test
    @Transactional
    @Rollback
    public void testGetUsers() throws Exception {
        User user = createAndSaveUser("foo@bar.com");

        MockHttpServletRequestBuilder request = get("/users")
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].email", equalTo(user.getEmail())));
    }

    @Test
    @Transactional
    @Rollback
    public void testPostUser() throws Exception {
        Long count = userRepository.count();

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("email", "foo@example.com");
            }
        };

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(payload));

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.email", equalTo("foo@example.com")));

        assertThat(userRepository.count(), equalTo(count + 1));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetRecentlyWatched() throws Exception {
        User user = createAndSaveUser("foo@bar.com");

        Show show = createAndSaveShow("Silicon Valley");

        Episode episode = createAndSaveEpisode(show.getId());

        Viewing viewing = new Viewing();
        viewing.setEpisodeId(episode.getId());
        viewing.setShowId(show.getId());
        viewing.setUserId(user.getId());
        viewing.setTimecode(65);
        viewing.setUpdatedAt(new Date());
        viewingRepository.save(viewing);

        MockHttpServletRequestBuilder request = get(
                String.format("/users/%d/recently-watched", user.getId()))
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("$[0].show.id", notNullValue()))
                .andExpect(jsonPath("$[0].show.name", equalTo("Silicon Valley")))
                .andExpect(jsonPath("$[0].episode.id", notNullValue()))
                .andExpect(jsonPath("$[0].episode.seasonNumber", equalTo(2)))
                .andExpect(jsonPath("$[0].episode.episodeNumber", equalTo(3)))
                .andExpect(jsonPath("$[0].episode.title", equalTo("S2 E3")))
                .andExpect(jsonPath("$[0].updatedAt", notNullValue()))
                .andExpect(jsonPath("$[0].timecode", equalTo(65)));
    }

    @Test
    @Transactional
    @Rollback
    public void testPatchViewings_createsANewViewing() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User user = createAndSaveUser("email@email.com");
        Show show = createAndSaveShow("Game of Thrones");
        Episode episode = createAndSaveEpisode(show.getId());

        Viewing existing = viewingRepository.findByUserIdAndEpisodeId(user.getId(), episode.getId());
        assertThat(existing, nullValue());

        ViewingWrapper wrapper = new ViewingWrapper(
                episode.getId(), new Date(), 79);

        MockHttpServletRequestBuilder request = patch(
                String.format("/users/%d/viewings", user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wrapper));

        mvc.perform(request).andExpect(status().isOk());

        Viewing actual = viewingRepository.findByUserIdAndEpisodeId(user.getId(), episode.getId());
        assertThat(actual, notNullValue());
        assertThat(actual.getEpisodeId(), equalTo(episode.getId()));
        assertThat(actual.getShowId(), equalTo(show.getId()));
        assertThat(actual.getUserId(), equalTo(user.getId()));
        assertThat(actual.getTimecode(), equalTo(79));
        assertThat(actual.getUpdatedAt(), notNullValue());
    }

    @Test
    @Transactional
    @Rollback
    public void testPatchViewings_updatesAnExistingViewing() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User user = createAndSaveUser("email@email.com");
        Show show = createAndSaveShow("Game of Thrones");
        Episode episode = createAndSaveEpisode(show.getId());
        Viewing viewing = createAndSaveViewing(user.getId(), episode.getId());

        Viewing existing = viewingRepository.findOne(viewing.getId());
        assertThat(existing, notNullValue());
        assertThat(existing.getTimecode(), equalTo(99));

        ViewingWrapper wrapper = new ViewingWrapper(episode.getId(), new Date(), 79);

        MockHttpServletRequestBuilder request = patch(
                String.format("/users/%d/viewings", user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wrapper));

        mvc.perform(request).andExpect(status().isOk());

        Viewing actual = viewingRepository.findByUserIdAndEpisodeId(user.getId(), episode.getId());
        assertThat(actual, notNullValue());
        assertThat(actual.getTimecode(), equalTo(79));
    }

    private Viewing createAndSaveViewing(Long userId, Long episodeId) {
        Viewing viewing = new Viewing();
        Show show = createAndSaveShow("Foobar");

        viewing.setShowId(show.getId());
        viewing.setTimecode(99);
        viewing.setUpdatedAt(new Date());
        viewing.setUserId(userId);
        viewing.setEpisodeId(episodeId);
        viewingRepository.save(viewing);

        return viewing;
    }

    private Show createAndSaveShow(String name) {
        Show show = new Show();
        show.setName(name);
        showRepository.save(show);

        return show;
    }

    private Episode createAndSaveEpisode(Long showId) {
        Episode episode = new Episode();
        episode.setSeasonNumber(2);
        episode.setEpisodeNumber(3);
        episode.setShowId(showId);
        episodeRepository.save(episode);

        return episode;
    }

    private User createAndSaveUser(String email) {
        User user = new User();
        user.setEmail(email);
        userRepository.save(user);
        return user;
    }

}
