package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film add(Film film);
    void remove(int id);
    Film update(Film film);
    List<Film> findAll();
    Optional<Film> findById(int id);
    List<Genre> getGenres();
    Optional<Genre> getGenreById(int id);
    List<Mpa> getMpa();
    Optional<Mpa> getMpaById(int id);
    int addLike(int filmId, long userId);
    int removeLike(int filmId, long userId);
    List<Film> getPopular(int count);
}
