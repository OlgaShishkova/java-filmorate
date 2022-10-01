package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    Optional<Genre> getGenreById(int id);

    List<Genre> getGenres();

    List<Genre> getGenresByFilmId(int id);
}
