package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Film add(Film film);
    void remove(int id);
    Film update(Film film);
    List<Film> findAll();
    Optional<Film> findById(int id);
    List<Film> getPopular(int count);
}
