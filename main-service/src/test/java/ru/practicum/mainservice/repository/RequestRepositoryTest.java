package ru.practicum.mainservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.mainservice.dto.request.EventParticipantCount;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.request.ApplicationStatus;
import ru.practicum.mainservice.entity.request.EventRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class RequestRepositoryTest extends GenerateUtilEntities {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private List<Event> events;

    private List<User> users;

    private List<EventRequest> requests;

    @BeforeEach
    void setUp() {

        users = userRepository.saveAll(generateUsers(2));

        List<Category> categories = categoryRepository.saveAll(generateCategories(3));

        events = eventRepository.saveAll(generateEvents(10, users, categories));

        requests = requestRepository.saveAll(List.of(
                EventRequest.builder()
                        .status(ApplicationStatus.PENDING)
                        .user(users.get(0))
                        .event(events.get(2))
                        .build(),
                EventRequest.builder()
                        .status(ApplicationStatus.PENDING)
                        .user(users.get(1))
                        .event(events.get(3))
                        .build(),
                EventRequest.builder()
                        .status(ApplicationStatus.PENDING)
                        .user(users.get(0))
                        .event(events.get(6))
                        .build(),
                EventRequest.builder()
                        .status(ApplicationStatus.PENDING)
                        .user(users.get(0))
                        .event(events.get(1))
                        .build(),
                EventRequest.builder()
                        .status(ApplicationStatus.PENDING)
                        .user(users.get(0))
                        .event(events.get(0))
                        .build(),
                EventRequest.builder()
                        .status(ApplicationStatus.PENDING)
                        .user(users.get(1))
                        .event(events.get(0))
                        .build()
        ));
    }

    @Test
    void shouldReturnAllRequests_whenEqualsUserIdAndEventId() {

        Long expectedEventId = events.getFirst().getId();
        Long expectedUserId = users.get(1).getId();

        List<EventRequest> findEventLocal = requests.stream()
                .filter(
                        r -> r.getEvent().getId().equals(expectedEventId) &&
                                r.getUser().getId().equals(expectedUserId)
                )
                .toList();

        boolean eventIdAndUserId = requestRepository.existsByEventIdAndUserId(expectedEventId, expectedUserId);

        Assertions.assertEquals(eventIdAndUserId, !findEventLocal.isEmpty());
    }

    @Test
    void shouldReturnAllRequests_whenEqualsUserId() {

        Long expectedUserId = users.getFirst().getId();

        List<EventRequest> expectedRequests = requests.stream()
                .filter(r -> r.getUser().getId().equals(expectedUserId))
                .toList();

        List<EventRequest> requests = requestRepository.findAllByUserId(expectedUserId);

        assertThat(requests)
                .isNotNull()
                .hasSize(expectedRequests.size())
                .allMatch(r -> r.getUser().getId().equals(expectedUserId));
    }

    @Test
    void shouldReturnAllRequests_whenEqualsEventId() {

        Long expectedEventId = events.getFirst().getId();

        List<EventRequest> expectedRequests = requests.stream()
                .filter(r -> r.getEvent().getId().equals(expectedEventId))
                .toList();

        List<EventRequest> requests = requestRepository.findAllByEventId(expectedEventId);

        assertThat(requests)
                .isNotNull()
                .hasSize(expectedRequests.size())
                .allMatch(r -> r.getEvent().getId().equals(expectedEventId));
    }

    @Test
    void shouldUpdateStatus_whenEqualsRequestIdAndEventId() {

        Long expectedEventId = events.getFirst().getId();

        List<Long> expectedReqIds = requests.stream()
                .filter(r -> r.getEvent().getId().equals(expectedEventId))
                .map(EventRequest::getId)
                .toList();

        requestRepository.updateRequestsStatus(expectedReqIds, ApplicationStatus.CONFIRMED, expectedEventId);

        List<EventParticipantCount> participants =
                requestRepository.getParticipantsByEventIdsAndStatus(List.of(expectedEventId), ApplicationStatus.CONFIRMED);

        Long participantsByEvent = participants.stream()
                .filter(e -> e.eventId().equals(expectedEventId))
                .findFirst()
                .map(EventParticipantCount::count)
                .orElse(0L);

        Assertions.assertEquals(expectedReqIds.size(), participantsByEvent);
    }
}