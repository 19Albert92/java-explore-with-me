package ru.practicum.mainservice.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.user.UserNotFoundException;
import ru.practicum.mainservice.mapper.UserMapper;
import ru.practicum.mainservice.repository.UserRepository;
import ru.practicum.mainservice.service.user.UserService;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAll(List<Long> ids, int from, int size) {

        Page<User> result;

        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Order.asc("id")));

        if (ids.isEmpty()) {
            result = userRepository.findAll(pageRequest);
        } else {
            result = userRepository.findByIdIn(ids, pageRequest);
        }

        return result.getContent().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    @Transactional
    public UserDto save(NewUserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.email())) {
            log.debug("User with email {} already exists", userRequest.email());
            throw new ConflictException(
                    "Integrity constraint has been violated.",
                    "Email already exists");
        }

        User newUser = UserMapper.mapToUser(userRequest);

        newUser = userRepository.save(newUser);

        log.info("New user has been saved: {}", newUser);

        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        findByIdOrException(userId);

        log.debug("User with id {} has been deleted", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto findById(Long userId) {
        log.info("User with id {} has been found", userId);
        return UserMapper.mapToUserDto(findByIdOrException(userId));
    }

    @Override
    public User findByIdOrException(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "The required object was not found.",
                        String.format("User with id=%d was not found", userId)
                ));
    }
}
