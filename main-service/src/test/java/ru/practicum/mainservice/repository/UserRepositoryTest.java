package ru.practicum.mainservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.mainservice.entity.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/clear_schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnedUser_ifExistsByEmail() {

        String expectedEmail = "example@google.com";

        userRepository.save(User.builder().email(expectedEmail).name("Petia").build());

        boolean existsByEmail = userRepository.existsByEmail(expectedEmail);

        Assertions.assertTrue(existsByEmail, "Должен найти пользователя по email так как он имеется");
    }

    @Test
    void shouldReturnListUsers_bySort() {

        long expectedId = 1L;
        String expectedEmail = "example@google.com";

        userRepository.saveAll(List.of(
                User.builder().email(expectedEmail).name("Petia").build(),
                User.builder().email("example2@google.com").name("Petia2").build(),
                User.builder().email("example3@google.com").name("Petia3").build()
        ));

        Page<User> result = userRepository.findByIdIn(List.of(1L, 2L),
                PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

        assertThat(result.getContent())
                .isNotNull()
                .hasSize(2)
                .first()
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("email", expectedEmail);
    }
}