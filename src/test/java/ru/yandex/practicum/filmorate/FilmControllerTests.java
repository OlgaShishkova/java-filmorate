package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTests {
    Film film;
    FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        film = new Film();
        film.setName("Film");
        film.setDescription("filmDescription");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1990, 2, 23));
        filmController = new FilmController();
    }

    @Test
    public void shouldAddFilm() {
        filmController.add(film);
        Assertions.assertEquals(filmController.getAll().size(), 1);
    }

    @Test
    public void shouldThrowFilmAlreadyExistException() {
        filmController.add(film);
        film.setName("newFilm");
        FilmAlreadyExistException ex = assertThrows(
                FilmAlreadyExistException.class,
                () -> filmController.add(film));
        assertEquals("Такой фильм уже существует.", ex.getMessage());
    }

    @Test
    public void shouldThrowFilmNotFoundException() {
        filmController.add(film);
        film.setName("newFilm");
        film.setId(-1);
        FilmNotFoundException ex = assertThrows(
                FilmNotFoundException.class,
                () -> filmController.update(film));
        assertEquals("Фильм не найден.", ex.getMessage());
    }
}
