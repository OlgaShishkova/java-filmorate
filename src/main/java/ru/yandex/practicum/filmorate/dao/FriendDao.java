package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendDao {

    int addFriend(long userId, long friendId);
    int removeFriend(long userId, long friendId);
    List<User> getFriends(long id);
    List<User> getCommonFriends(long userId, long friendId);
}
