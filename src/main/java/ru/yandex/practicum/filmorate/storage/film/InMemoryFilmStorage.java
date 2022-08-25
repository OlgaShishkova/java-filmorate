package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film add(Film film) {
        film.setId(++filmId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void remove(int id) {
        films.remove(id);
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(int id) {
        return Optional.ofNullable(films.get(id));
    }

}
