package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private int filmId;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (films.containsValue(film)) {
            throw new FilmAlreadyExistException("Такой фильм уже существует.");
        }
        film.setId(++filmId);
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: '{}'", film.getName());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм не найден.");
        }
        films.put(film.getId(), film);
        log.debug("Обновлена информация о фильме: '{}'", film.getName());
        return film;
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
    }

    @ExceptionHandler(FilmNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleException(FilmNotFoundException exception) {
        log.error(exception.getMessage(), exception);
    }
}
