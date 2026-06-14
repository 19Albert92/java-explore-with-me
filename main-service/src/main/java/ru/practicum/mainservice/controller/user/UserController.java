package ru.practicum.mainservice.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(
            @RequestParam(required = false, defaultValue = "") List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return userService.findAll(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest userRequest) {
        return userService.save(userRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
