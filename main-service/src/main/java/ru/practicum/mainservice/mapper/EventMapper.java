package ru.practicum.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.event.*;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.event.EventState;

import java.time.LocalDateTime;

import static shared.UtilConstant.FORMATTER;

@UtilityClass
public class EventMapper {

    public static Event toEvent(User initiator, Category category, NewEventDto eventDto) {
        return Event.builder()
                .initiator(initiator)
                .annotation(eventDto.annotation())
                .category(category)
                .description(eventDto.description())
                .eventDate(eventDto.eventDate())
                .location(eventDto.location())
                .paid(eventDto.paid())
                .participantLimit(eventDto.participantLimit())
                .requestModeration(eventDto.requestModeration() == null || eventDto.requestModeration())
                .title(eventDto.title())
                .state(EventState.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Event toEvent(Event event, Category category, UpdateEventAdminDto eventAdminDto) {

        if (eventAdminDto.hasEventDate()) {
            event.setEventDate(eventAdminDto.eventDate());
        }

        if (eventAdminDto.hasLocation()) {
            event.setLocation(eventAdminDto.location());
        }

        if (eventAdminDto.hasPaid()) {
            event.setPaid(eventAdminDto.paid());
        }

        if (eventAdminDto.hasParticipantLimit()) {
            event.setParticipantLimit(eventAdminDto.participantLimit());
        }

        if (eventAdminDto.hasRequestModeration()) {
            event.setRequestModeration(eventAdminDto.requestModeration());
        }

        if (eventAdminDto.hasTitle()) {
            event.setTitle(eventAdminDto.title());
        }

        if (eventAdminDto.hasAnnotation()) {
            event.setAnnotation(eventAdminDto.annotation());
        }

        if (eventAdminDto.hasCategory() && category != null) {
            event.setCategory(category);
        }

        if (eventAdminDto.hasPaid()) {
            event.setPaid(eventAdminDto.paid());
        }

        if (eventAdminDto.hasDescription()) {
            event.setDescription(eventAdminDto.description());
        }

        if (eventAdminDto.hasState()) {
            event.setState(eventAdminDto.parseAdminState());
        }

        return  event;
    }

    public static Event toEvent(Event event, Category category, UpdateEventDto eventDto) {

        if (eventDto.hasEventDate()) {
            event.setEventDate(eventDto.eventDate());
        }

        if (eventDto.hasLocation()) {
            event.setLocation(eventDto.location());
        }

        if (eventDto.hasPaid()) {
            event.setPaid(eventDto.paid());
        }

        if (eventDto.hasParticipantLimit()) {
            event.setParticipantLimit(eventDto.participantLimit());
        }

        if (eventDto.hasRequestModeration()) {
            event.setRequestModeration(eventDto.requestModeration());
        }

        if (eventDto.hasTitle()) {
            event.setTitle(eventDto.title());
        }

        if (eventDto.hasAnnotation()) {
            event.setAnnotation(eventDto.annotation());
        }

        if (eventDto.hasCategory() && category != null) {
            event.setCategory(category);
        }

        if (eventDto.hasPaid()) {
            event.setPaid(eventDto.paid());
        }

        if (eventDto.hasDescription()) {
            event.setDescription(eventDto.description());
        }

        if (eventDto.hasStateAction()) {
            event.setState(eventDto.parseUpdatedState());
        }

        return  event;
    }

    public static EventFullDto toEventFullDto(Event event, long confirmRequest, long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapToCategoryDto(event.getCategory()))
                .confirmedRequests(confirmRequest)
                .location(LocationMapper.mapToLocationDto(event.getLocation()))
                .createdOn(event.getCreatedAt().format(FORMATTER))
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn().format(FORMATTER))
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .description(event.getDescription())
                .views(views)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event, long confirmRequest, long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapToCategoryDto(event.getCategory()))
                .confirmedRequests(confirmRequest)
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
