package ru.yandex.practicum.filmorate.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "delete from users where user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> findAll() {
            String sqlQuery = "select * from users";
            return jdbcTemplate.query(sqlQuery, UserDaoImpl::mapRowToUser);
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            String sqlQuery = "select * from users where user_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, UserDaoImpl::mapRowToUser, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected static User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        if (resultSet != null) {
            return User.builder()
                    .id(resultSet.getLong("user_id"))
                    .email(resultSet.getString("email"))
                    .login(resultSet.getString("login"))
                    .name(resultSet.getString("name"))
                    .birthday(resultSet.getDate("birthday").toLocalDate())
                    .build();
        } else {
            return null;
        }
    }
}
