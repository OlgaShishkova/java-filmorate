package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;

@Component
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addLike(int filmId, long userId) {
        String sqlQuery = "insert into film_likes(film_id, user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return jdbcTemplate.queryForObject(
                "select count(*) from film_likes where film_id = ?", Integer.class, filmId);
    }

    @Override
    public int removeLike(int filmId, long userId) {
        String sqlQuery = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return jdbcTemplate.queryForObject(
                "select count(*) from film_likes where film_id = ?", Integer.class, filmId);
    }
}
