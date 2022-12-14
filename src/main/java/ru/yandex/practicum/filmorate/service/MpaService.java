package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.MpaDao;

import java.util.List;

@Service
public class MpaService {
    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> getMpa() {
        return mpaDao.getMpa();
    }
    public Mpa getMpaById(int id) {
        return mpaDao.getMpaById(id).orElseThrow(() -> new NotFoundException("Значение рейтинга не найдено"));
    }
}
