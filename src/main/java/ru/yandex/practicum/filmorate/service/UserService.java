package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;
    private final FriendDao friendDao;

    @Autowired
    public UserService(UserDao userDao, FriendDao friendDao) {
        this.userDao = userDao;
        this.friendDao = friendDao;
    }

    public int addFriend(long userId, long friendId) {
        if (userDao.findById(userId).isEmpty() ||
        userDao.findById(friendId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return friendDao.addFriend(userId, friendId);
    }

    public int removeFriend(long userId, long friendId) {
        return friendDao.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long id) {
        return friendDao.getFriends(id);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return friendDao.getCommonFriends(userId, friendId);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findById(long id) {
        return userDao.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
    }

    public User create(User user) {
        return userDao.create(user);
    }

    public User update(User user) {
        findById(user.getId());
        return userDao.update(user);
    }

    public void delete(long id) {
        userDao.delete(id);
    }
}
