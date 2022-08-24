package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    public final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public int addFriend(long userId, long friendId) {
        if (userStorage.findById(userId).isPresent() &&
            userStorage.findById(friendId).isPresent()) {
                if (userId != friendId) {
                    userStorage.findById(friendId).get().getFriends().add(userId);
                    userStorage.findById(userId).get().getFriends().add(friendId);
                }
                return userStorage.findById(userId).get().getFriends().size();
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    public int deleteFriend(long userId, long friendId) {
        if (userStorage.findById(userId).isPresent() &&
            userStorage.findById(friendId).isPresent()) {
            userStorage.findById(friendId).get().getFriends().remove(userId);
            return userStorage.findById(userId).get().getFriends().size();
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    public List<User> getFriends(Long id) {
        if(userStorage.findById(id).isPresent()) {
            List<User> friends = new ArrayList<>();
            for (Long friendId : userStorage.findById(id).get().getFriends()) {
                friends.add(userStorage.findById(friendId).get());
            }
            return friends;
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        List<User> commonFriends = new ArrayList<>();
        if(userStorage.findById(userId).isPresent() &&
           userStorage.findById(friendId).isPresent()) {
            Set<Long> userFriends = new HashSet<>(userStorage.findById(userId).get().getFriends());
            for (Long id : userStorage.findById(friendId).get().getFriends()) {
                if (!userFriends.add(id)) {
                    commonFriends.add(userStorage.findById(id).get());
                }
            }
        }
        return commonFriends;
    }
}
