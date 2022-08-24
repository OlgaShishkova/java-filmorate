package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        return filmService.filmStorage.add(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return filmService.filmStorage.update(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.filmStorage.findAll();
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable int id) {
        filmService.filmStorage.remove(id);
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        return filmService.filmStorage.findById(id).orElseThrow(() -> new FilmNotFoundException("Фильм не найден"));
    }

    @PutMapping("/{id}/like/{userId}")
    public int addLike(@PathVariable int id, @PathVariable long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public int deleteLike(@PathVariable int id, @PathVariable long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10", required = false) int count) {
        return filmService.getPopular(count);
    }
}
