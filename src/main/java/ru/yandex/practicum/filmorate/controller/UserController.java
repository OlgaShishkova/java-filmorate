package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int userId;

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        user.setId(++userId);
        checkName(user);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user.getEmail());
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        if(!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        checkName(user);
        users.put(user.getId(), user);
        log.info("Обновлен пользователь: {}", user.getEmail());
        return user;
    }

    private void checkName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }
}
