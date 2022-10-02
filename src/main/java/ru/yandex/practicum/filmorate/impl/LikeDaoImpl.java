package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDao;

@Repository
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addLike(int filmId, long userId) {
        String sqlQuery = "insert into film_likes(film_id, user_id) " +
                "values (?, ?)";
        return updateFilmLikesTable(sqlQuery, filmId, userId);
    }

    @Override
    public int removeLike(int filmId, long userId) {
        String sqlQuery = "delete from film_likes where film_id = ? and user_id = ?";
        return updateFilmLikesTable(sqlQuery, filmId, userId);
    }

    private int updateFilmLikesTable(String sqlQuery, int filmId, long userId) {
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return jdbcTemplate.queryForObject(
                "select count(*) from film_likes where film_id = ?", Integer.class, filmId);
    }

}
