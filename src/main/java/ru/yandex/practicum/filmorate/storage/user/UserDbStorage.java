package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
                .usingGeneratedKeyColumns("user_id");
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
        String sqlQuery = "delete from users where user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public User update(User user) {
        checkName(user);
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

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
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

    @Override
    public List<User> findAll() {
            String sqlQuery = "select * from users";
            return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            String sqlQuery = "select * from users where user_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int addFriend(long userId, long friendId) {
        String sqlQuery = "select count(*) from user_friends where user_id = ? and friend_id = ?";
        if(jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId, friendId) == 0) {
            sqlQuery = "insert into user_friends(user_id, friend_id) values (?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        }
        return jdbcTemplate.queryForObject(
                "select count(*) from user_friends where user_id = ?", Integer.class, userId);
    }

    @Override
    public int removeFriend(long userId, long friendId) {
        String sqlQuery = "delete from user_friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return jdbcTemplate.queryForObject(
                "select count(*) from user_friends where user_id = ?", Integer.class, userId);
    }

    @Override
    public List<User> getFriends(long id) {
        String sqlQuery = "select u.user_id, u.email, u.login, u.name, u.birthday " +
                "from user_friends as uf " +
                "left join users u on u.user_id = uf.friend_id" +
                " where uf.user_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
    }
}
