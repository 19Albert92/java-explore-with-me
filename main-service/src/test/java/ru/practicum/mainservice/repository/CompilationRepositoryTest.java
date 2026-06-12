package ru.practicum.mainservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.mainservice.MockGeneratedData;
import ru.practicum.mainservice.config.QueryDslTestConfig;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.Compilation;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.repository.query.CompilationQueryRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({QueryDslTestConfig.class, CompilationQueryRepository.class})
class CompilationRepositoryTest extends GenerateUtilEntities {

    @Autowired
    private CompilationQueryRepository queryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CompilationRepository compilationRepository;

    private List<Compilation> compilations;

    @BeforeEach
    void setUp() {

        List<Category> categories = categoryRepository.saveAll(generateCategories(3));
        List<User> users = userRepository.saveAll(generateUsers(2));

        List<Event> events = eventRepository.saveAll(generateEvents(3, users, categories));
        List<Event> events2 = eventRepository.saveAll(generateEvents(4, users, categories));
        List<Event> events3 = eventRepository.saveAll(generateEvents(2, users, categories));

        compilations = compilationRepository.saveAll(List.of(
                Compilation.builder()
                        .title(MockGeneratedData.generatorText(10)).pinned(false).events(events)
                        .build(),
                Compilation.builder()
                        .title(MockGeneratedData.generatorText(8)).pinned(false).events(events2)
                        .build(),
                Compilation.builder()
                        .title(MockGeneratedData.generatorText(8)).pinned(true).events(events3)
                        .build()
        ));
    }

    @Test
    void shouldReturnCompilationList_whenExistsPinned() {

        boolean expectedPinned = true;

        Compilation findCompilation = compilations.stream()
                .filter(Compilation::isPinned)
                .findFirst()
                .orElse(new Compilation());

        List<Compilation> compilationByPinedFilter =
                queryRepository.findCompilationByPinedFilter(expectedPinned, 0, 10);

        assertThat(compilationByPinedFilter)
                .hasSize(1)
                .first()
                .satisfies(compilation ->
                        assertThat(compilation)
                                .hasFieldOrPropertyWithValue("pinned", expectedPinned)
                                .hasFieldOrPropertyWithValue("events", findCompilation.getEvents())
                                .hasFieldOrPropertyWithValue("title", findCompilation.getTitle())
                );
    }

    @Test
    void shouldReturnCompilationList_whenExistsNotPinned() {

        boolean expectedPinned = false;
        int expectedCount = 2;

        List<Compilation> findCompilation = compilations.stream()
                .filter(c -> !c.isPinned())
                .toList();

        Compilation expectedFirst = findCompilation.getFirst();

        List<Compilation> compilationByPinedFilter =
                queryRepository.findCompilationByPinedFilter(expectedPinned, 0, 10);

        assertThat(compilationByPinedFilter)
                .hasSize(expectedCount)
                .first()
                .satisfies(compilation ->
                        assertThat(compilation)
                                .hasFieldOrPropertyWithValue("pinned", expectedPinned)
                                .hasFieldOrPropertyWithValue("events", expectedFirst.getEvents())
                                .hasFieldOrPropertyWithValue("title", expectedFirst.getTitle())
                );
    }

    @Test
    void shouldReturnEmptyList_whenNotCompilationMatch() {
        List<Compilation> compilationByPinedFilter =
                queryRepository.findCompilationByPinedFilter(true, 1, 10);

        assertThat(compilationByPinedFilter).hasSize(0);
    }
}