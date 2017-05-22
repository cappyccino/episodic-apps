package com.example.episodicevents.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventsControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setup() {
        eventsRepository.deleteAll();
    }

    @Test
    public void testGetRecent_returnsOnly() throws Exception {
        for (int i = 0; i < 30; i++) {
            eventsRepository.save(new PlayEvent());
        }

        MockHttpServletRequestBuilder request = get("/recent")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(20)));
    }

    @Test
    public void testPostEvent_withFastForward() throws Exception {
        Map<String, Object> data = new HashMap<String, Object>(){
            {
                put("startOffset", 1);
                put("endOffset", 2);
                put("speed", 1.5);
            }
        };
        String payload = getPayload("fastForward", data);

        MockHttpServletRequestBuilder request = getPostRequest(payload);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", equalTo("fastForward")))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.showId", equalTo(2)))
                .andExpect(jsonPath("$.episodeId", equalTo(3)))
                .andExpect(jsonPath("$.createdAt", equalTo("2017-11-08T15:59:13.0091745")))
                .andExpect(jsonPath("$.data.startOffset", equalTo(1)))
                .andExpect(jsonPath("$.data.endOffset", equalTo(2)))
                .andExpect(jsonPath("$.data.speed", equalTo(1.5)));
    }

    @Test
    public void testPostEvent_withPause() throws Exception {
        String payload = getPayload("pause", singletonMap("offset", 2));

        MockHttpServletRequestBuilder request = getPostRequest(payload);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", equalTo("pause")))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.showId", equalTo(2)))
                .andExpect(jsonPath("$.episodeId", equalTo(3)))
                .andExpect(jsonPath("$.createdAt", equalTo("2017-11-08T15:59:13.0091745")))
                .andExpect(jsonPath("$.data.offset", equalTo(2)));
    }

    @Test
    public void testPostEvent_withPlay() throws Exception {
        String payload = getPayload("play", singletonMap("offset", 2));

        MockHttpServletRequestBuilder request = getPostRequest(payload);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", equalTo("play")))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.showId", equalTo(2)))
                .andExpect(jsonPath("$.episodeId", equalTo(3)))
                .andExpect(jsonPath("$.createdAt", equalTo("2017-11-08T15:59:13.0091745")))
                .andExpect(jsonPath("$.data.offset", equalTo(2)));
    }

    @Test
    public void testPostEvent_withProgress() throws Exception {
        String payload = getPayload("progress", singletonMap("offset", 2));

        MockHttpServletRequestBuilder request = getPostRequest(payload);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", equalTo("progress")))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.showId", equalTo(2)))
                .andExpect(jsonPath("$.episodeId", equalTo(3)))
                .andExpect(jsonPath("$.createdAt", equalTo("2017-11-08T15:59:13.0091745")))
                .andExpect(jsonPath("$.data.offset", equalTo(2)));
    }

    @Test
    public void testPostEvent_withRewind() throws Exception {
        Map<String, Object> data = new HashMap<String, Object>(){
            {
                put("startOffset", 1);
                put("endOffset", 2);
                put("speed", 1.5);
            }
        };
        String payload = getPayload("rewind", data);

        MockHttpServletRequestBuilder request = getPostRequest(payload);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", equalTo("rewind")))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.showId", equalTo(2)))
                .andExpect(jsonPath("$.episodeId", equalTo(3)))
                .andExpect(jsonPath("$.createdAt", equalTo("2017-11-08T15:59:13.0091745")))
                .andExpect(jsonPath("$.data.startOffset", equalTo(1)))
                .andExpect(jsonPath("$.data.endOffset", equalTo(2)))
                .andExpect(jsonPath("$.data.speed", equalTo(1.5)));
    }

    @Test
    public void testPostEvent_withScrub() throws Exception {
        Map<String, Object> data = new HashMap<String, Object>(){
            {
                put("startOffset", 1);
                put("endOffset", 2);
            }
        };
        String payload = getPayload("scrub", data);

        MockHttpServletRequestBuilder request = getPostRequest(payload);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", equalTo("scrub")))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.showId", equalTo(2)))
                .andExpect(jsonPath("$.episodeId", equalTo(3)))
                .andExpect(jsonPath("$.createdAt", equalTo("2017-11-08T15:59:13.0091745")))
                .andExpect(jsonPath("$.data.startOffset", equalTo(1)))
                .andExpect(jsonPath("$.data.endOffset", equalTo(2)));
    }

    private MockHttpServletRequestBuilder getPostRequest(String payload) {
        return post("/")
                    .content(payload)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
    }

    private String getPayload(String type, Map data) throws JsonProcessingException {
        Map map = new HashMap<String, Object>() {
                {
                    put("type", type);
                    put("userId", 1L);
                    put("showId", 2L);
                    put("episodeId", 3L);
                    put("createdAt", new Date());
                    put("data", data);
                }
            };
        return objectMapper.writeValueAsString(map);
    }
}
