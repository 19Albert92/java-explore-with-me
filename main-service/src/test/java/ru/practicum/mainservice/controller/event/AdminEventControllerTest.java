package ru.practicum.mainservice.controller.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.MockGeneratedData;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.QueryEventsDto;
import ru.practicum.mainservice.dto.event.UpdateEventAdminDto;
import ru.practicum.mainservice.service.event.EventService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminEventController.class)
class AdminEventControllerTest {

    private static final String BASE_URL = "/admin/events";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @Test
    void shouldReturnEventList_whenMatcherQueryParams() throws Exception {

        List<EventFullDto> events = List.of(
                EventFullDto.builder().annotation(MockGeneratedData.generatorText(40)).build(),
                EventFullDto.builder().annotation(MockGeneratedData.generatorText(50)).build(),
                EventFullDto.builder().annotation(MockGeneratedData.generatorText(20)).build()
        );

        when(eventService.findAll(any(QueryEventsDto.class))).thenReturn(events);

        mockMvc.perform(
                get(BASE_URL)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(eventService).findAll(any(QueryEventsDto.class));
    }

    @Test
    void shouldReturnNewEvent_whenUpdateAdminSuccessfully() throws Exception {

        String expectedAnnotation = MockGeneratedData.generatorText(40);

        long eventId = 1L;

        UpdateEventAdminDto eventAdminDto = UpdateEventAdminDto.builder()
                .title(MockGeneratedData.generatorText(30))
                .description(MockGeneratedData.generatorText(200))
                .build();

        EventFullDto eventFullDto = EventFullDto.builder()
                .annotation(expectedAnnotation)
                .paid(true)
                .build();

        when(eventService.updateEventByEventId(eventId, eventAdminDto)).thenReturn(eventFullDto);

        mockMvc.perform(
                        patch(BASE_URL + "/{eventId}", eventId)
                                .content(objectMapper.writeValueAsString(eventAdminDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.annotation").value(expectedAnnotation));

        verify(eventService).updateEventByEventId(eventId, eventAdminDto);
    }
}