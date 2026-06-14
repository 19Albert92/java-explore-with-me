package ru.practicum.mainservice.service.user;

import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.exception.user.UserNotFoundException;

import java.util.List;

public interface UserService {

    List<UserDto> findAll(List<Long> ids, int from, int size);

    UserDto save(NewUserRequest userRequest);

    void delete(Long userId);

    UserDto findById(Long userId);

    User findByIdOrException(Long userId) throws UserNotFoundException;
}
