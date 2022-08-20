package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);
    void delete(int id);
    User update(User user);
    Collection<User> findAll();
}
