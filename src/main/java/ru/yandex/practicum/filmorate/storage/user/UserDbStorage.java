package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        checkName(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    private void checkName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
    @Override
    public void delete(long id) {
        String sqlQuery = "delete from users where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public User update(User user) {
        checkName(user);
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public List<User> findAll() {
            String sqlQuery = "select id, email, login, name, birthday from users";
            return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public Optional<User> findById(long id) {
        String sqlQuery = "select * from users where id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id));
        /*SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if(userRows.next()) {
            return Optional.of(User.builder()
                    .id(userRows.getLong("id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(userRows.getDate("birthday").toLocalDate())
                    .build());
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }*/
    }
}
