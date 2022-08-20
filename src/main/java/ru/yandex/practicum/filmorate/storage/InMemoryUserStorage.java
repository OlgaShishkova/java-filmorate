package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int userId;

    @Override
    public User create(User user) {
        user.setId(++userId);
        checkName(user);
        users.put(user.getId(), user);
        return user;
    }

    private void checkName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }

    @Override
    public User update(User user) {
        if(!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        checkName(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }
}
