package ru.practicum.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.entity.User;

@UtilityClass
public class UserMapper {

    public User mapToUser(NewUserRequest userRequest) {
        return User.builder()
                .email(userRequest.email())
                .name(userRequest.name())
                .build();
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public UserShortDto mapToUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
