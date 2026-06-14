package ru.practicum.mainservice.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.request.ApplicationStatus;
import ru.practicum.mainservice.entity.request.EventRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RequestMappingTest {

    private static final User USER = User.builder().id(1L).name("Vasia").build();

    private static final Event EVENT = Event.builder().id(1L).initiator(USER).build();

    private static final ApplicationStatus STATUS = ApplicationStatus.PENDING;

    private static final EventRequest REQUEST = EventRequest.builder()
            .created(LocalDateTime.now())
            .event(EVENT).status(STATUS).user(USER).build();

    @Test
    void mapToEventRequestTest() {

        EventRequest request = RequestMapper.mapToEventRequest(USER, EVENT, STATUS);

        assertThat(request)
                .isNotNull()
                .hasFieldOrPropertyWithValue("user", USER)
                .hasFieldOrPropertyWithValue("status", STATUS)
                .hasFieldOrPropertyWithValue("event", EVENT);
    }

    @Test
    void mapToRequestDto() {

        RequestDto requestDto = RequestMapper.mapToRequestDto(REQUEST);

        assertThat(requestDto)
                .isNotNull()
                .hasFieldOrPropertyWithValue("requester", USER.getId())
                .hasFieldOrPropertyWithValue("event", EVENT.getId())
                .hasFieldOrPropertyWithValue("status", STATUS);
    }

    @Test
    void mapToEventRequestWithCancelStatus() {

        EventRequest requestDto = RequestMapper.mapToEventRequestWithCancelStatus(REQUEST);

        assertThat(requestDto)
                .isNotNull()
                .hasFieldOrPropertyWithValue("user", USER)
                .hasFieldOrPropertyWithValue("event", EVENT);
    }
}