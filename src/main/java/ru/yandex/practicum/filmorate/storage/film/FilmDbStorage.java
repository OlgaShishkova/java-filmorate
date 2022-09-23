package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Override
    public Film add(Film film) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Optional<Film> findById(int id) {
        return Optional.empty();
    }
}
