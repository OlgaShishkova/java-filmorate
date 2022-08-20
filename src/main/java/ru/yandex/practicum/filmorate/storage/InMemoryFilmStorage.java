package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film add(Film film) {
        if (films.containsValue(film)) {
            throw new FilmAlreadyExistException("Такой фильм уже существует.");
        }
        film.setId(++filmId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void remove(int filmId) {
        films.remove(filmId);
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм не найден.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }
}
