package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Validated
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int userId;

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException("Такой пользователь уже существует.");
        }
        user.setId(++userId);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user.getEmail());
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        if(!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        users.put(user.getId(), user);
        log.info("Обновлен пользователь: {}", user.getEmail());
        return user;
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleException(UserNotFoundException exception) {
        log.error(exception.getMessage(), exception);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(UserAlreadyExistException exception) {
        log.error(exception.getMessage(), exception);
    }
}
