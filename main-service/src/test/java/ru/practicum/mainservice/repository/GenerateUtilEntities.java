package ru.practicum.mainservice.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.mainservice.MockGeneratedData;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.event.EventState;
import ru.practicum.mainservice.entity.event.Location;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@DataJpaTest
public abstract class GenerateUtilEntities {

    protected static final Random random = new Random();

    protected List<Category> generateCategories(int limit) {
        return Stream.generate(() ->
                        Category.builder().name(MockGeneratedData.generatorText(random.nextInt(50))).build())
                .limit(limit)
                .toList();
    }

    protected List<User> generateUsers(int limit) {
        return  Stream.generate(() ->
                        User.builder().name(MockGeneratedData.generatorText(random.nextInt(20)))
                                .email(MockGeneratedData.generatorEmail()).build())
                .limit(limit)
                .toList();
    }

    protected List<Event> generateEvents(int limit, List<User> users, List<Category> categories) {

        EventState[] states = EventState.values();

        return Stream.generate(() ->
                        Event.builder()
                                .title(MockGeneratedData.generatorText(10))
                                .initiator(users.get(random.nextInt(users.size())))
                                .eventDate(LocalDateTime.now().plusDays(random.nextInt(1, 10)))
                                .annotation(MockGeneratedData.generatorText(100))
                                .description(MockGeneratedData.generatorText(150))
                                .paid(random.nextBoolean())
                                .category(categories.get(random.nextInt(categories.size())))
                                .participantLimit(random.nextInt(10))
                                .requestModeration(true)
                                .location(new Location(random.nextFloat(), random.nextFloat()))
                                .createdAt(LocalDateTime.now())
                                .state(states[random.nextInt(states.length)])
                                .build())
                .limit(limit)
                .toList();
    }
}
