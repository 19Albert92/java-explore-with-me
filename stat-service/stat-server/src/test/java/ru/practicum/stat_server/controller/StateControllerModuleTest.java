package ru.practicum.stat_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stat_server.service.StateService;
import shared.dto.EndpointHit;
import shared.dto.ViewStats;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StateController.class)
class StateControllerModuleTest {

    private final String BASE_URL = "http://localhost:9090";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StateService stateService;

    @Test
    void shouldReturnedStats_whenValidGetRequest() throws Exception {

        String expectedApp = "ewm-main-service";
        int expectedResultSize = 2;
        long expectedHits = 4L;

        List<ViewStats> states = List.of(
                new ViewStats(expectedApp, "/events/1", expectedHits),
                new ViewStats(expectedApp, "/events/51", 2L)
        );

        Mockito.when(stateService.getStateByFilter(Mockito.any())).thenReturn(states);

        mockMvc.perform(get(BASE_URL + "/stats")
                        .param("start", "2020-05-05 00:00:00")
                        .param("end", "2022-11-15 23:59:59")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedResultSize))
                .andExpect(jsonPath("$.[0].app").value(expectedApp))
                .andExpect(jsonPath("$.[0].hits").value(expectedHits));

        Mockito.verify(stateService, Mockito.times(1)).getStateByFilter(Mockito.any());
    }

    @Test
    void shouldSaveHit_whenValidEndpointHit() throws Exception {

        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp("ewm-main-service");
        endpointHit.setIp("127.0.0.1");
        endpointHit.setTimestamp("2021-05-05 23:59:59");
        endpointHit.setUri("/events/1");

        mockMvc.perform(post(BASE_URL + "/hit")
                .content(objectMapper.writeValueAsString(endpointHit))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Mockito.verify(stateService, Mockito.times(1)).saveHit(endpointHit);
    }
}