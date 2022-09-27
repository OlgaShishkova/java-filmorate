package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        String sqlQuery = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                sqlQuery = "insert into film_genre(film_id, genre_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQuery,
                        film.getId(),
                        genre.getId());
            }
        }
    }

    @Override
    public void remove(int id) {
        String sqlQuery = "delete from films where film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
        sqlQuery = "delete from film_genre where film_id = ?";
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
                .mpa(Mpa.builder()
                        .id(resultSet.getInt("mpa_id"))
                        .name(resultSet.getString("mpa_name"))
                        .build())
                .genres(findGenresByFilmId(resultSet.getInt("film_id")))
                .build();
    }

    private List<Genre> findGenresByFilmId(int id) {
        String sqlQuery = "select fg.genre_id, g.genre_name from film_genre as fg " +
                "left join genres as g on g.genre_id = fg.genre_id " +
                "where film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
    }
    @Override
    public List<Film> findAll() {
        String sqlQuery = "select * from films as f " +
                "left join mpa as m on f.mpa_id = m.mpa_id ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> findById(int id) {
        try {
            String sqlQuery = "select * from films as f " +
                    "left join mpa as m on f.mpa_id = m.mpa_id " +
                    "where film_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }

    @Override
    public List<Genre> getGenres() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }


    @Override
    public Optional<Genre> getGenreById(int id) {
        try {
            String sqlQuery = "select * from genres where genre_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }

    @Override
    public List<Mpa> getMpa() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        try {
            String sqlQuery = "select * from mpa where mpa_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int addLike(int filmId, long userId) {
        String sqlQuery = "select count(*) from film_likes where film_id = ? and user_id = ?";
        if (jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, userId) == 0) {
            sqlQuery = "insert into film_likes(film_id, user_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery, filmId, userId);
        }
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
