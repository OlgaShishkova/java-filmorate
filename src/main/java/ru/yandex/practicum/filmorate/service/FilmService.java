package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final LikeDao likeDao;

    @Autowired
    public FilmService(FilmDao filmDao, UserDao userDao, LikeDao likeDao) {
        this.filmDao = filmDao;
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

    public List<Film> getPopular(int count) {
        return filmDao.getPopular(count);
    }

    public Film findById(int id) {
        return filmDao.findById(id).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public List<Film> findAll() {
        return filmDao.findAll();
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


}
