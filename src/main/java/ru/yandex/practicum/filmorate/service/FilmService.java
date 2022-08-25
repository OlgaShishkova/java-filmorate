package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public int addLike(int filmId, long userId) {
        if(userStorage.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        findById(filmId).getLikes().add(userId);
        return findById(filmId).getLikes().size();
    }

    public int deleteLike(int filmId, long userId) {
        if(userStorage.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        findById(filmId).getLikes().remove(userId);
        return findById(filmId).getLikes().size();
    }

    public List<Film> getPopular(int count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findById(int id) {
        return filmStorage.findById(id).orElseThrow(() -> new FilmNotFoundException("Фильм не найден"));
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film add(Film film) {
        if (film.getId() != null && filmStorage.findById(film.getId()).isPresent()) {
            throw new FilmAlreadyExistException("Такой фильм уже существует");
        }
        return filmStorage.add(film);
    }

    public void remove(int id) {
        filmStorage.remove(id);
    }

    public Film update(Film film) {
        if (filmStorage.findById(film.getId()).isEmpty()) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        return filmStorage.update(film);
    }
}
