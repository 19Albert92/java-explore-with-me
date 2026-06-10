package ru.practicum.mainservice.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.service.user.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/admin/users";

    @Test
    void shouldReturnedStatus201_whenUserSavedSuccessfully() throws Exception {

        final String expectedEmail = "example@gmail.com";
        final String expectedName = "text";

        NewUserRequest newUserRequest = NewUserRequest.builder().email(expectedEmail).name(expectedName).build();

        UserDto userDto = UserDto.builder().email(expectedEmail).name(expectedName).build();

        Mockito.when(userService.save(newUserRequest)).thenReturn(userDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(newUserRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(expectedEmail))
                .andExpect(jsonPath("$.name").value(expectedName));

        Mockito.verify(userService).save(newUserRequest);
    }

    @Test
    void shouldReturnedUserDtoList_whenFinedByFilter() throws Exception {

        String expectedEmail = "example1@google.com";

        List<UserDto> users = List.of(
                UserDto.builder().email(expectedEmail).name("User1").build(),
                UserDto.builder().email("example2@google.com").name("User2").build(),
                UserDto.builder().email("example3@google.com").name("User3").build()
        );

        Mockito.when(userService.findAll(List.of(), 0, 10)).thenReturn(users);

        mockMvc.perform(get(BASE_URL)
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].email").value(expectedEmail));

        Mockito.verify(userService).findAll(List.of(), 0, 10);
    }

    @Test
    void shouldReturnedStatus204_whenUserDeletedSuccessfully() throws Exception {

        long userId = 10L;

        Mockito.when(userService.findByIdOrException(userId)).thenReturn(new User());

        mockMvc.perform(delete(BASE_URL + "/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isNoContent());

        Mockito.verify(userService).delete(userId);
    }
}