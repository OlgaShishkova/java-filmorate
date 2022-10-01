package ru.yandex.practicum.filmorate.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        try {
            String sqlQuery = "select * from genres where genre_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, GenreDaoImpl::mapRowToGenre, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getGenres() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, GenreDaoImpl::mapRowToGenre);
    }

    @Override
    public List<Genre> getGenresByFilmId(int id) {
        String sqlQuery = "select fg.genre_id, g.genre_name from film_genres as fg " +
                "left join genres as g on g.genre_id = fg.genre_id " +
                "where film_id = ?";
        return jdbcTemplate.query(sqlQuery, GenreDaoImpl::mapRowToGenre, id);
    }

    protected static Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}
