package ru.practicum.mainservice.controller.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.service.request.RequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrivateRequestController.class)
class PrivateRequestControllerTest {

    private static final String BASE_URL = "/users/{userId}/requests";

    private static final long USER_ID = 1L;
    private static final long REQUEST_ID = 5L;
    private static final long EVENT_ID = 2L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Test
    void shouldReturnListEventRequest_whenMatcherUserId() throws Exception {

        long expectedRequestId = 1L;

        List<RequestDto> requests = List.of(
                RequestDto.builder().id(expectedRequestId).build(),
                RequestDto.builder().id(2L).build()
        );

        when(requestService.findAllRequestByCurrentUser(USER_ID)).thenReturn(requests);

        mockMvc.perform(
                        get(BASE_URL, USER_ID)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(requests.size()))
                .andExpect(jsonPath("$[0].id").value(expectedRequestId));

        verify(requestService).findAllRequestByCurrentUser(USER_ID);
    }

    @Test
    void shouldReturnNewEventRequest_whenCreateSuccessfully() throws Exception {

        RequestDto requestDto = RequestDto.builder().id(REQUEST_ID).build();

        when(requestService.saveRequestToEvent(USER_ID, EVENT_ID)).thenReturn(requestDto);

        mockMvc.perform(
                        post(BASE_URL, USER_ID)
                                .param("eventId", Long.toString(EVENT_ID))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(REQUEST_ID));

        verify(requestService).saveRequestToEvent(USER_ID, EVENT_ID);
    }

    @Test
    void shouldReturnNewEventRequest_whenUpdateSuccessfully() throws Exception {

        RequestDto requestDto = RequestDto.builder().id(REQUEST_ID).build();

        when(requestService.cancelRequestToEvent(USER_ID, REQUEST_ID)).thenReturn(requestDto);

        mockMvc.perform(
                        patch(BASE_URL + "/{requestId}/cancel", USER_ID, REQUEST_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(REQUEST_ID));

        verify(requestService).cancelRequestToEvent(USER_ID, REQUEST_ID);
    }
}