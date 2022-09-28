package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("userMemoryStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long userId = 0L;

    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

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
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public User update(User user) {
        checkName(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public int addFriend(long userId, long friendId) {
        return 0;
    }

    @Override
    public int removeFriend(long userId, long friendId) {
        return 0;
    }

    @Override
    public List<User> getFriends(long id) {
        return null;
    }
}
