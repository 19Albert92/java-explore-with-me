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
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.dto.event.UpdateEventDto;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.dto.request.UpdateRequestStatusDto;
import ru.practicum.mainservice.dto.request.UpdateRequestStatusRequestDto;
import ru.practicum.mainservice.entity.event.Location;
import ru.practicum.mainservice.entity.request.ApplicationStatus;
import ru.practicum.mainservice.service.event.EventService;
import ru.practicum.mainservice.service.request.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrivateEventController.class)
class PrivateEventControllerTest {

    private static final String BASE_URL = "/users/{userId}/events";

    private static final Long USER_ID = 1L;
    private static final Long EVENT_ID = 10L;

    private static final String EXPECTED_TITLE = MockGeneratedData.generatorText(30);
    private static final String EXPECTED_ANNOTATION = MockGeneratedData.generatorText(100);
    private static final String EXPECTED_DESCRIPTION = MockGeneratedData.generatorText(100);

    private static final EventFullDto EVENT_FULL_DTO = EventFullDto.builder()
            .title(EXPECTED_TITLE)
            .annotation(EXPECTED_ANNOTATION)
            .description(EXPECTED_DESCRIPTION)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @MockBean
    private RequestService requestService;

    @Test
    void shouldEventsList_whenIfExistsByInitiatorId() throws Exception {

        int from = 0;
        int size = 10;

        List<EventShortDto> eventsShortDto = List.of(
                EventShortDto.builder().id(EVENT_ID).build(),
                EventShortDto.builder().id(2L).build(),
                EventShortDto.builder().id(3L).build()
        );

        when(eventService.findAllByUserId(USER_ID, from, size)).thenReturn(eventsShortDto);

        mockMvc.perform(
                        get(BASE_URL, USER_ID)
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(eventsShortDto.size()))
                .andExpect(jsonPath("$[0].id").value(EVENT_ID));

        verify(eventService).findAllByUserId(USER_ID, from, size);
    }

    @Test
    void shouldNewEvent_whenCreatedSuccessfully() throws Exception {

        NewEventDto newEventDto = NewEventDto.builder()
                .title(EXPECTED_TITLE)
                .annotation(EXPECTED_ANNOTATION)
                .description(EXPECTED_DESCRIPTION)
                .eventDate(LocalDateTime.now().plusHours(3))
                .category(1)
                .location(new Location(0.1f, 0.2f))
                .build();

        when(eventService.save(anyLong(), any(NewEventDto.class))).thenReturn(EVENT_FULL_DTO);

        mockMvc.perform(
                        post(BASE_URL, USER_ID)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newEventDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(EXPECTED_TITLE))
                .andExpect(jsonPath("$.annotation").value(EXPECTED_ANNOTATION))
                .andExpect(jsonPath("$.description").value(EXPECTED_DESCRIPTION));

        verify(eventService).save(anyLong(), any(NewEventDto.class));
    }

    @Test
    void shouldStatus400_whenNewEventNotValidate() throws Exception {

        NewEventDto newEventDto = NewEventDto.builder()
                .title(EXPECTED_TITLE)
                .annotation(EXPECTED_ANNOTATION)
                .description(EXPECTED_DESCRIPTION)
                .eventDate(LocalDateTime.now())
                .category(1)
                .location(new Location(0.1f, 0.2f))
                .build();

        when(eventService.save(anyLong(), any(NewEventDto.class))).thenReturn(EVENT_FULL_DTO);

        mockMvc.perform(
                        post(BASE_URL, USER_ID)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newEventDto))
                )
                .andExpect(status().isBadRequest());

        verify(eventService, never()).save(anyLong(), any(NewEventDto.class));
    }

    @Test
    void shouldFullEvent_whenFindById() throws Exception {

        when(eventService.findEventByUserId(EVENT_ID, USER_ID)).thenReturn(EVENT_FULL_DTO);

        mockMvc.perform(
                        get(BASE_URL + "/{eventId}", USER_ID, EVENT_ID)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(EXPECTED_TITLE))
                .andExpect(jsonPath("$.annotation").value(EXPECTED_ANNOTATION))
                .andExpect(jsonPath("$.description").value(EXPECTED_DESCRIPTION));

        verify(eventService).findEventByUserId(EVENT_ID, USER_ID);
    }

    @Test
    void shouldNewEvent_whenUpdatedSuccessfully() throws Exception {

        UpdateEventDto updateEventDto = UpdateEventDto.builder().build();

        when(eventService.update(EVENT_ID, USER_ID, updateEventDto)).thenReturn(EVENT_FULL_DTO);

        mockMvc.perform(
                        patch(BASE_URL + "/{eventId}", USER_ID, EVENT_ID)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateEventDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(EXPECTED_TITLE))
                .andExpect(jsonPath("$.annotation").value(EXPECTED_ANNOTATION))
                .andExpect(jsonPath("$.description").value(EXPECTED_DESCRIPTION));

        verify(eventService).update(EVENT_ID, USER_ID, updateEventDto);
    }

    @Test
    void shouldRequests_whenMatcherRequesterId() throws Exception {

        List<RequestDto> requestDtoList = List.of(
                RequestDto.builder().id(1L).status(ApplicationStatus.CONFIRMED).build(),
                RequestDto.builder().id(3L).status(ApplicationStatus.CANCELED).build(),
                RequestDto.builder().id(2L).status(ApplicationStatus.CONFIRMED).build()
        );

        when(requestService.findRequestsByUserId(EVENT_ID, USER_ID)).thenReturn(requestDtoList);

        mockMvc.perform(
                        get(BASE_URL + "/{eventId}/requests", USER_ID, EVENT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(requestDtoList.size()));

        verify(requestService).findRequestsByUserId(EVENT_ID, USER_ID);
    }

    @Test
    void shouldNewRequest_whenUpdatedSuccessfully() throws Exception {

        UpdateRequestStatusRequestDto updateRequest =
                new UpdateRequestStatusRequestDto(List.of(1L, 2L),  ApplicationStatus.CONFIRMED);

        List<RequestDto> confirmedRequests = List.of(
                RequestDto.builder().id(1L).build(),
                RequestDto.builder().id(2L).build()
        );

        UpdateRequestStatusDto updateRequestStatusDto =
                new UpdateRequestStatusDto(confirmedRequests, List.of());

        when(requestService.updateRequestStatus(USER_ID, EVENT_ID, updateRequest)).thenReturn(updateRequestStatusDto);

        mockMvc.perform(
                        patch(BASE_URL + "/{eventId}/requests", USER_ID, EVENT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.confirmedRequests.length()").value(confirmedRequests.size()));

        verify(requestService).updateRequestStatus(USER_ID, EVENT_ID, updateRequest);
    }

}