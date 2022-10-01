package ru.yandex.practicum.filmorate.dao;

public interface LikeDao {
    int addLike(int filmId, long userId);
    int removeLike(int filmId, long userId);
}
