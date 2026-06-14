package ru.practicum.mainservice.controller.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.FilterEventDto;
import ru.practicum.mainservice.service.event.EventService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicEventController.class)
class PublicEventControllerTest {

    private static final String BASE_URL = "/events";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    void shouldReturnPublishedEvents_whenMatchesFilter() throws Exception {

        List<EventShortDto> events = List.of(
                EventShortDto.builder().id(1L).build(),
                EventShortDto.builder().id(2L).build()
        );

        when(eventService.findAll(any(FilterEventDto.class), anyString(), anyString())).thenReturn(events);

        mockMvc.perform(
                        get(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(events.size()));

        verify(eventService).findAll(any(FilterEventDto.class), anyString(), anyString());
    }

    @Test
    void shouldReturnEvent_whenIfExistsById() throws Exception {

        long expectedId = 1L;

        EventFullDto eventFullDto = EventFullDto.builder().id(expectedId).build();

        when(eventService.findById(anyLong(), anyString(), anyString())).thenReturn(eventFullDto);

        mockMvc.perform(
                        get(BASE_URL + "/{id}", expectedId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId));

        verify(eventService).findById(anyLong(), anyString(), anyString());
    }
}