package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public int addFriend(long userId, long friendId) {
        if (userStorage.findById(userId).isEmpty() ||
        userStorage.findById(friendId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.addFriend(userId, friendId);
    }

    public int removeFriend(long userId, long friendId) {
        return userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        List<User> commonFriends = userStorage.getFriends(userId);
        commonFriends.retainAll(userStorage.getFriends(friendId));
        return commonFriends;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(long id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        findById(user.getId());
        return userStorage.update(user);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }
}
