package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public int addFriend(long userId, long friendId) {
        if (userId != friendId) {
            findById(friendId).getFriends().add(userId);
            findById(userId).getFriends().add(friendId);
        }
        return findById(userId).getFriends().size();
    }

    public int deleteFriend(long userId, long friendId) {
        findById(friendId).getFriends().remove(userId);
        findById(userId).getFriends().remove(friendId);
        return findById(userId).getFriends().size();
    }

    public List<User> getFriends(long id) {
        return findById(id).getFriends().stream().map(this::findById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        List<Long> commonFriends = new ArrayList<>(findById(userId).getFriends());
        commonFriends.retainAll(findById(friendId).getFriends());
        return commonFriends.stream().map(this::findById).collect(Collectors.toList());
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(long id) {
        return userStorage.findById(id).orElseThrow(() -> new FilmNotFoundException("Пользователь не найден"));
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
