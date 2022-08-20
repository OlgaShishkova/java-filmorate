package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@Validated
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserController(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }


    @PostMapping
    public User create(@RequestBody @Valid User user) {
//        log.info("Добавлен пользователь: {}", user.getEmail());
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
//        log.info("Обновлен пользователь: {}", user.getEmail());
        return userStorage.update(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable @NotNull int userId) {
        userStorage.delete(userId);
    }

    @GetMapping
    public Collection<User> getAll() {
//        log.info("Текущее количество пользователей: {}", users.size());
        return userStorage.findAll();
    }
}
