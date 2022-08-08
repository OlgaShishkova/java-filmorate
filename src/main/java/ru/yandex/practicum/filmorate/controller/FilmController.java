package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private int filmId;
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
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
}
