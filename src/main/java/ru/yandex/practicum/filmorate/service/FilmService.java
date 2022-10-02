package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final UserDao userDao;
    private final LikeDao likeDao;

    @Autowired
    public FilmService(FilmDao filmDao, GenreDao genreDao, UserDao userDao, LikeDao likeDao) {
        this.filmDao = filmDao;
        this.genreDao = genreDao;
        this.userDao = userDao;
        this.likeDao = likeDao;
    }

    public int addLike(int filmId, long userId) {
        if(userDao.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        if(filmDao.findById(filmId).isEmpty()) {
            throw new NotFoundException("Фильм не найден");
        }
        return likeDao.addLike(filmId, userId);
    }

    public int removeLike(int filmId, long userId) {
        if(userDao.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        if(filmDao.findById(filmId).isEmpty()) {
            throw new NotFoundException("Фильм не найден");
        }
        return likeDao.removeLike(filmId, userId);
    }

    public Film findById(int id) {
        Optional<Film> film = filmDao.findById(id);
        if(filmDao.findById(id).isEmpty()) {
            throw new NotFoundException("Фильм не найден");
        }
        film.get().setGenres(genreDao.getGenresByFilmId(id));
        return film.get();
    }

    public List<Film> findAll() {
        List<Film> films = filmDao.findAll();
        setGenres(films);
        return films;
    }

    public List<Film> getPopular(int count) {
        List<Film> films = filmDao.getPopular(count);
        setGenres(films);
        return films;
    }

    public Film add(Film film) {
        if (film.getId() != null && filmDao.findById(film.getId()).isPresent()) {
            throw new FilmAlreadyExistException("Такой фильм уже существует");
        }
        return filmDao.add(film);
    }

    public void remove(int id) {
        filmDao.remove(id);
    }

    public Film update(Film film) {
        if (filmDao.findById(film.getId()).isEmpty()) {
            throw new NotFoundException("Фильм не найден");
        }
        return filmDao.update(film);
    }

    private void setGenres(List<Film> films) {
        for (Film film : films) {
            film.setGenres(genreDao.getGenresByFilmId(film.getId()));
        }
    }
}
