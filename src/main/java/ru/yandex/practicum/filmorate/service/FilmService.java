package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public int addLike(int filmId, long userId) {
        if(filmStorage.findById(filmId).isEmpty()) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        if(userStorage.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Фильм не найден");
        }
        filmStorage.findById(filmId).get().getLikes().add(userId);
        return filmStorage.findById(filmId).get().getLikes().size();
    }

    public int deleteLike(int filmId, long userId) {
        if(filmStorage.findById(filmId).isEmpty()) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        if(userStorage.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Фильм не найден");
        }
        filmStorage.findById(filmId).get().getLikes().remove(userId);
        return filmStorage.findById(filmId).get().getLikes().size();
    }

    public List<Film> getPopular(int count) {
        List<Film> popularFilms;
        popularFilms = filmStorage.findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
        return popularFilms;
    }
}
