package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage filmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
//        log.debug("Добавлен фильм: '{}'", film.getName());
        return filmStorage.add(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
//        log.debug("Обновлена информация о фильме: '{}'", film.getName());
        return filmStorage.update(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
//        log.debug("Текущее количество фильмов: {}", films.size());
        return filmStorage.findAll();
    }

    @DeleteMapping("/{filmId}")
    public void remove(@PathVariable @NotNull int filmId) {
        filmStorage.remove(filmId);
    }
}
