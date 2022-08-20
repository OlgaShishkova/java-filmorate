package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film add(Film film);
    void remove(int filmId);
    Film update(Film film);
    Collection<Film> findAll();
}
