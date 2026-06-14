package ru.practicum.mainservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.mainservice.config.QueryDslTestConfig;
import ru.practicum.mainservice.dto.event.FilterEventDto;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.event.EventState;
import ru.practicum.mainservice.repository.query.EventQueryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Import({QueryDslTestConfig.class, EventQueryRepository.class})
class EventRepositoryTest extends GenerateUtilEntities {

    @Autowired
    private EventQueryRepository eventQueryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private List<Event> events;

    private List<User> users;

    private List<Category> categories;

    @BeforeEach
    void setUp() {

        users = userRepository.saveAll(generateUsers(2));

        categories = categoryRepository.saveAll(generateCategories(3));

        events = eventRepository.saveAll(generateEvents(12,  users, categories));
    }

    @Test
    void shouldReturnEventList_whenInitiatorEquals() {

        Long expectedInitiatorId = users.getFirst().getId();

        List<Event> expectedEvents = events.stream()
                .filter(e -> e.getInitiator().getId().equals(expectedInitiatorId))
                .toList();

        Page<Event> eventsByInitiatorId = eventRepository
                .findAllByInitiatorId(expectedInitiatorId, PageRequest.of(0, 10));

        assertThat(eventsByInitiatorId.getContent())
                .isNotNull()
                .hasSize(expectedEvents.size());
    }

    @Test
    void shouldReturnEvent_whenInitiatorIdAndEventIdEquals() {

        User expectedUser = users.getFirst();
        Event expectedEvent = events.getFirst();

        Optional<Event> findEventLocal = events.stream()
                .filter(e -> e.getInitiator().equals(expectedUser) && e.getId().equals(expectedEvent.getId()))
                .findFirst();

        Optional<Event> findEvent = eventRepository
                .findByIdAndInitiatorId(expectedEvent.getId(), expectedUser.getId());

        assertEquals(findEvent, findEventLocal);

        findEvent.ifPresent(event -> assertEquals(event.getInitiator(), expectedUser));
    }

    @Test
    void shouldReturnEventList_whenInIds() {

        Event firstEvent = events.getFirst();
        Event lastEvent = events.getLast();

        List<Long> expectedEvents = List.of(firstEvent.getId(), lastEvent.getId());

        List<Event> allByIdLocal = events.stream()
                .filter(e -> expectedEvents.contains(e.getId()))
                .toList();

        List<Event> allById = eventRepository.findAllById(expectedEvents);

        assertThat(allById)
                .isNotNull()
                .hasSize(allByIdLocal.size());

        if (!allById.isEmpty()) {
            assertThat(allById)
                    .first()
                    .hasFieldOrPropertyWithValue("id", firstEvent.getId())
                    .hasFieldOrPropertyWithValue("annotation", firstEvent.getAnnotation());
        }
    }

    @Test
    void shouldReturnTrue_whenEvenFromCategoryExists() {

        Category category = categories.get(random.nextInt(categories.size()));

        List<Event> allByCategory = events.stream()
                .filter(e -> e.getCategory().equals(category))
                .toList();

        boolean existsByCategoryId = eventRepository.existsByCategoryId(category.getId());

        assertEquals(!allByCategory.isEmpty(), existsByCategoryId);
    }

    @Test
    void shouldReturnEventList_whenSearchByFilterText() {

        String searchText = "e";

        FilterEventDto filterEventDto = new FilterEventDto(
                searchText,
                null, null, null, null, null, null,
                0, 10
        );

        findEventByFilter(filterEventDto, events.stream()
                .filter(c -> c.getState() == EventState.PUBLISHED)
                .filter(e -> e.getAnnotation().toLowerCase().contains(searchText) ||
                        e.getDescription().toLowerCase().contains(searchText))
                .toList());
    }

    @Test
    void shouldReturnEventList_whenSearchByFilterCategory() {

        List<Long> filterCategoryIds = List.of(0L, 1L);

        FilterEventDto filterEventDto = new FilterEventDto(
                null, filterCategoryIds, null, null, null, null, null,
                0, 10
        );

        findEventByFilter(filterEventDto, events.stream()
                .filter(c -> c.getState() == EventState.PUBLISHED)
                .filter(c -> filterCategoryIds.contains(c.getCategory().getId()))
                .toList());
    }

    @Test
    void shouldReturnEventList_whenSearchByFilterPaid() {

        FilterEventDto filterEventDto = new FilterEventDto(
                null, null, true, null, null, null, null,
                0, 10
        );

        findEventByFilter(filterEventDto, events.stream()
                .filter(c -> c.getState() == EventState.PUBLISHED)
                .filter(Event::isPaid)
                .toList());
    }

    @Test
    void shouldReturnEventList_whenFindByFilter() {

        LocalDateTime now = LocalDateTime.now();

        FilterEventDto filterEventDto = new FilterEventDto(
                null, null, null, now, null, null, null,
                0, 10
        );

        findEventByFilter(filterEventDto, events.stream()
                .filter(c -> c.getState() == EventState.PUBLISHED)
                .filter(c -> c.getEventDate().isAfter(now))
                .toList());
    }

    private void findEventByFilter(FilterEventDto filter, List<Event> expectedEvents) {

        List<Event> eventsByFilter = eventQueryRepository.findEventsByFilter(filter);

        Assertions.assertEquals(expectedEvents.size(), eventsByFilter.size(),
                String.format("By filter %s not found", filter));
    }
}