package ru.practicum.mainservice.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.entity.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserMapperTest {

    private static final String EMAIL = "exampl@google.com";
    private static final String NAME = "Petia";
    private static final long ID = 1;

    @Test
    void mapToUserFieldsTest() {
        NewUserRequest newUserRequest = NewUserRequest.builder().email(EMAIL).name(NAME).build();

        User newUser = UserMapper.mapToUser(newUserRequest);

        assertThat(newUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("email", EMAIL)
                .hasFieldOrPropertyWithValue("name", NAME);
    }

    @Test
    void mapToUserDtoTest() {

        User user = User.builder().id(ID).name(NAME).email(EMAIL).build();

        UserDto userDto = UserMapper.mapToUserDto(user);

        assertThat(userDto)
                .isNotNull()
                .hasFieldOrPropertyWithValue("email", EMAIL)
                .hasFieldOrPropertyWithValue("id", ID)
                .hasFieldOrPropertyWithValue("name", NAME);
    }
}