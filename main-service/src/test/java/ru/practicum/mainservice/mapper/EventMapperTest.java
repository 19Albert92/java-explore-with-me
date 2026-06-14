package ru.practicum.mainservice.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.dto.event.UpdateEventAdminDto;
import ru.practicum.mainservice.dto.event.UpdateEventDto;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.event.EventState;
import ru.practicum.mainservice.entity.request.AdminStateAction;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EventMapperTest {

    private static final String TITLE = "Юбилейный концерт «30 нам уже!»";
    private static final String ANNOTATION =
            "Юбилейный концерт «30 нам уже!», приуроченный к 30-летию легендарной группы «Руки Вверх!».»";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final Category CATEGORY = Category.builder().id(1L).name("Концерт").build();

    @Test
    void mapToEventTest() {

        User initiator = User.builder().id(1L).name("Vasiliy").build();

        NewEventDto newEventDto = NewEventDto.builder()
                .title(TITLE)
                .annotation(ANNOTATION)
                .eventDate(NOW)
                .build();

        Event event = EventMapper.toEvent(initiator, CATEGORY, newEventDto);

        assertThat(event)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", TITLE)
                .hasFieldOrPropertyWithValue("annotation", ANNOTATION)
                .hasFieldOrPropertyWithValue("eventDate", NOW);
    }

    @Test
    void mapToEventAdminUpdatedTest() {

        String updateTitle = TITLE + "_admin_updated";

        EventState newState = EventState.PUBLISHED;

        Event event = Event.builder()
                .eventDate(NOW)
                .title(TITLE)
                .annotation(ANNOTATION)
                .build();

        UpdateEventAdminDto updateEventAdminDto = UpdateEventAdminDto.builder()
                .title(updateTitle)
                .state(AdminStateAction.PUBLISH_EVENT)
                .build();

        Event newEvent = EventMapper.toEvent(event, CATEGORY, updateEventAdminDto);

        assertThat(newEvent)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", updateTitle)
                .hasFieldOrPropertyWithValue("annotation", ANNOTATION)
                .hasFieldOrPropertyWithValue("state", newState)
                .hasFieldOrPropertyWithValue("eventDate", NOW);
    }

    @Test
    void mapToEventUpdatedTest() {

        String updateTitle = TITLE + "_updated";

        Event event = Event.builder()
                .eventDate(NOW)
                .title(TITLE)
                .annotation(ANNOTATION)
                .build();

        UpdateEventDto updateEventAdminDto = UpdateEventDto.builder()
                .title(updateTitle)
                .build();

        Event newEvent = EventMapper.toEvent(event, CATEGORY, updateEventAdminDto);

        assertThat(newEvent)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", updateTitle)
                .hasFieldOrPropertyWithValue("annotation", ANNOTATION)
                .hasFieldOrPropertyWithValue("eventDate", NOW);
    }

    @Test
    void mapToEventFullDtoTest() {

        long expectedConfirmRequest = 1L;
        long expectedViews = 3L;

        User initiator = User.builder().id(1L).name("Vasiliy").build();

        Event event = Event.builder()
                .initiator(initiator)
                .category(CATEGORY)
                .eventDate(NOW.minusHours(2))
                .title(TITLE)
                .annotation(ANNOTATION)
                .createdAt(NOW.minusDays(2))
                .build();

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event, expectedConfirmRequest, expectedViews);

        assertThat(eventFullDto)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", TITLE)
                .hasFieldOrPropertyWithValue("annotation", ANNOTATION)
                .hasFieldOrPropertyWithValue("confirmedRequests", expectedConfirmRequest)
                .hasFieldOrPropertyWithValue("views", expectedViews);
    }
}