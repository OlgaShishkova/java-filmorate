package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTests {
    User user;
    UserController userController;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setName("Name");
        user.setLogin("Login");
        user.setEmail("name@mail.ru");
        user.setBirthday(LocalDate.of(1960, 5, 25));
        userController = new UserController();
    }

    @Test
    public void shouldCreateUser() {
        userController.create(user);
        Assertions.assertEquals(userController.getAll().size(), 1);
    }

    @Test
    public void shouldThrowUserAlreadyExistException() {
        userController.create(user);
        user.setName("newName");
        UserAlreadyExistException ex = assertThrows(
                UserAlreadyExistException.class,
                () -> userController.create(user));
        assertEquals("Такой пользователь уже существует.", ex.getMessage());
    }

    @Test
    public void shouldThrowUserNotFoundException() {
        userController.create(user);
        user.setName("newName");
        user.setEmail("newEmail");
        user.setId(-1);
        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> userController.update(user));
        assertEquals("Пользователь не найден.", ex.getMessage());
    }

}
