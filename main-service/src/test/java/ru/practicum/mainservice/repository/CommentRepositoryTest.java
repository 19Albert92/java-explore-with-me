package ru.practicum.mainservice.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.MockGeneratedData;
import ru.practicum.mainservice.config.QueryDslTestConfig;
import ru.practicum.mainservice.dto.comment.CommentFilterParams;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.Comment;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.repository.query.CommentQueryRepository;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
@Import({QueryDslTestConfig.class, CommentQueryRepository.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentRepositoryTest extends GenerateUtilEntities {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentQueryRepository commentQueryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private List<Event> events;

    private List<Comment> comments;

    @BeforeAll
    @Transactional
    void setUp() {

        List<User> users = userRepository.saveAll(generateUsers(6));

        List<Category> categories = categoryRepository.saveAll(generateCategories(5));

        events = eventRepository.saveAll(generateEvents(10, users, categories));

        comments = commentRepository.saveAll(List.of(
                Comment.builder().user(users.get(1)).event(events.get(1)).text(MockGeneratedData.generatorText(20)).build(),
                Comment.builder().user(users.get(4)).event(events.get(0)).text(MockGeneratedData.generatorText(20)).build(),
                Comment.builder().user(users.get(1)).event(events.get(2)).text(MockGeneratedData.generatorText(20)).build(),
                Comment.builder().user(users.get(0)).event(events.get(1)).text(MockGeneratedData.generatorText(20)).build(),
                Comment.builder().user(users.get(3)).event(events.get(2)).text(MockGeneratedData.generatorText(20)).build(),
                Comment.builder().user(users.get(1)).event(events.get(0)).text(MockGeneratedData.generatorText(20)).build()
        ));
    }

    @Test
    void shouldReturnedAllComments_whenCommentMatchedByUserIds() {

        Event expectedEvent = events.get(1);

        CommentFilterParams commentFilterParams = CommentFilterParams.builder()
                .userIds(Set.of(1L))
                .build();

        List<Comment> expectedComments = comments.stream()
                .filter(comment -> comment.getEvent().getId().equals(expectedEvent.getId()))
                .filter(comment -> comment.getUser().getId().equals(1L))
                .toList();

        List<Comment> commentsByFilter = commentQueryRepository
                .findAllByFilter(expectedEvent.getId(), commentFilterParams);

        assertThat(commentsByFilter)
                .hasSize(expectedComments.size())
                .anyMatch(comment -> comment.getEvent().getId().equals(expectedEvent.getId()))
                .first()
                .hasFieldOrPropertyWithValue("text", expectedComments.getFirst().getText())
                .hasFieldOrPropertyWithValue("id", expectedComments.getFirst().getId());
    }

    @Test
    void shouldReturnedAllComments_whenCommentMatchedByFromSize() {

        Event expectedEvent = events.get(1);

        CommentFilterParams commentFilterParams = CommentFilterParams.builder()
                .from(0)
                .size(1)
                .build();

        List<Comment> commentsByFilter = commentQueryRepository
                .findAllByFilter(expectedEvent.getId(), commentFilterParams);

        assertThat(commentsByFilter)
                .hasSize(1)
                .anyMatch(comment -> comment.getEvent().getId().equals(expectedEvent.getId()));
    }
}