package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FriendDaoImpl implements FriendDao {

    private final JdbcTemplate jdbcTemplate;

    public FriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addFriend(long userId, long friendId) {
        String sqlQuery = "insert into user_friends(user_id, friend_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
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
        String sqlQuery = "select * " +
                "from users as u " +
                "left join user_friends as uf on u.user_id = uf.friend_id " +
                "where uf.user_id = ? ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sqlQuery = "select * " +
                "from users as u " +
                "left join user_friends as uf on u.user_id = uf.friend_id " +
                "where uf.user_id = ? " +
                "and friend_id in " +
                "(select friend_id from user_friends where user_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, friendId);
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
}
