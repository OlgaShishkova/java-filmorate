package ru.yandex.practicum.filmorate.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.GenreDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component//("filmDbStorage")
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        updateFilmGenreTable(film);
        return film;
    }

    private void updateFilmGenreTable(Film film) {
        String sqlQuery = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        if (film.getGenres() != null) {
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
            sqlQuery = "insert into film_genres(film_id, genre_id) " +
                    "values (?, ?)";
            jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, film.getGenres().get(i).getId());
                }
                public int getBatchSize() {
                    return film.getGenres().size();
                }
            });
        }
    }

    @Override
    public void remove(int id) {
        String sqlQuery = "delete from films where film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
        sqlQuery = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
        sqlQuery = "delete from film_likes where film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        updateFilmGenreTable(film);
        return film;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa (resultSet.getInt("mpa_id"),
                              resultSet.getString("mpa_name")))
                .genres(genreDao.getGenresByFilmId(resultSet.getInt("film_id")))
                .build();
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "select * from films as f " +
                "left join mpa as m on f.mpa_id = m.mpa_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> findById(int id) {
        try {
            String sqlQuery = "select * from films as f " +
                    "left join mpa as m on f.mpa_id = m.mpa_id " +
                    "where film_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getPopular (int count) {
        String sqlQuery = "select f.*, m.*, count(fl.user_id) as likes_count " +
                "from films as f " +
                "left join mpa as m on f.mpa_id = m.mpa_id " +
                "left join film_likes as fl on fl.film_id = f.film_id " +
                "group by f.film_id " +
                "order by likes_count desc " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

}
