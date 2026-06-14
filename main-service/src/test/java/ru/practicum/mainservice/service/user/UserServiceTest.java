package ru.practicum.mainservice.service.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.user.UserNotFoundException;
import ru.practicum.mainservice.repository.UserRepository;
import ru.practicum.mainservice.service.user.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private static final String FIRST_EXPECTED_EMAIL = "example@google.com";

    @Test
    void shouldReturnedAllUsers_by_filter() {

        List<Long> ids = List.of(1L, 2L);

        Page<User> users = new PageImpl<>(List.of(
                User.builder().email(FIRST_EXPECTED_EMAIL).name("Petia").build(),
                User.builder().email("vasia@google.com").name("Vasia").build()
        ));

        Mockito.when(userRepository.findByIdIn(Mockito.eq(ids), any(Pageable.class))).thenReturn(users);

        List<UserDto> finedUsers = userService.findAll(List.of(1L, 2L), 0, 10);

        assertThat(finedUsers)
                .isNotNull()
                .hasSize(2)
                .first()
                .hasFieldOrPropertyWithValue("email", FIRST_EXPECTED_EMAIL);

        Mockito.verify(userRepository).findByIdIn(Mockito.eq(ids), any(Pageable.class));
    }

    @Test
    void shouldReturnedNewUserDto_whenCreatingNewUserSuccessfully() {

        NewUserRequest newUserRequest = NewUserRequest.builder().email(FIRST_EXPECTED_EMAIL).build();

        User newUser = User.builder().email(FIRST_EXPECTED_EMAIL).build();

        Mockito.when(userRepository.existsByEmail(FIRST_EXPECTED_EMAIL)).thenReturn(false);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(newUser);

        UserDto savedUser = userService.save(newUserRequest);

        assertThat(savedUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("email", FIRST_EXPECTED_EMAIL);

        Mockito.verify(userRepository).existsByEmail(FIRST_EXPECTED_EMAIL);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void shouldReturnedConflictException_whenEmailAlreadyExists() {

        NewUserRequest newUserRequest = NewUserRequest.builder().email(FIRST_EXPECTED_EMAIL).build();

        Mockito.when(userRepository.existsByEmail(FIRST_EXPECTED_EMAIL)).thenThrow(ConflictException.class);

        Assertions.assertThrows(ConflictException.class, () -> userService.save(newUserRequest));

        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    @Test
    void shouldReturnedNotFoundException_whenUserNotFoundFromDelete() {

        long id = 1L;

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,() -> userService.delete(id));

        Mockito.verify(userRepository, Mockito.never()).deleteById(id);
    }
}