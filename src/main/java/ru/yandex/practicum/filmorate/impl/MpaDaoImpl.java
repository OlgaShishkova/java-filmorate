package ru.yandex.practicum.filmorate.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpa() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, MpaDaoImpl::mapRowToMpa);
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        try {
            String sqlQuery = "select * from mpa where mpa_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, MpaDaoImpl::mapRowToMpa, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected static Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }
}
