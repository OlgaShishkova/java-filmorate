package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);
    void delete(long id);
    User update(User user);
    List<User> findAll();
    Optional<User> findById(long id);
}
