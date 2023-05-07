package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private static long ids = 1;
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO PUBLIC.USER_DATA(USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES(?, ?, ?, ?, ?)";

        user.setId(ids++);
        jdbcTemplate.update(sql,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        return user;
    }

    @Override
    public User update(User user) throws NotFoundException {
        String sql = "UPDATE PUBLIC.USER_DATA SET " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";

        String exist = "SELECT COUNT(*) FROM USER_DATA WHERE USER_ID = ?";

        if (!isExist(exist, user.getId())) {
            throw new NotFoundException();
        }

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public void delete(long userId) {
        String sql = "DELETE FROM PUBLIC.USER_DATA WHERE USER_ID = ?";
        jdbcTemplate.update(sql, userId);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USER_DATA";

        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    public User findOne(long userId) {
        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM PUBLIC.USER_DATA WHERE USER_ID = ?";

        return jdbcTemplate.queryForObject(sql, this::mapRowToUser, userId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNumber) throws SQLException {
        User user = User.builder()
                .id(resultSet.getLong("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();

        List<Long> friends = jdbcTemplate.query("SELECT * FROM FRIEND WHERE USER_ID = ?",
                (rs, rowNum) -> rs.getLong("FRIEND_ID"), user.getId());

        if (!friends.isEmpty()) {
            for (long i : friends) {
                user.addFriend(i);
            }
        }

        return user;
    }

    public User getUser(long id) throws NotFoundException {
        String sql = "SELECT COUNT(*) FROM USER_DATA WHERE USER_ID = ?";

        if (!isExist(sql, id)) {
            throw new NotFoundException();
        }

        return findOne(id);
    }

    public void addFriend(long userId, long friendId) throws NotFoundException {
        String sql = "SELECT COUNT(*) FROM USER_DATA WHERE USER_ID = ?";

        if (!isExist(sql, friendId) || !isExist(sql, userId)) {
            throw new NotFoundException();
        }

        User user = findOne(userId);

        if (!user.getFriends().contains(friendId)) {
            jdbcTemplate.update("INSERT INTO FRIEND(USER_ID, FRIEND_ID) VALUES (?, ?) ", userId, friendId);
        }

        user.addFriend(friendId);
    }

    public void removeFriend(long userId, long friendId) throws ValidationException {
        String sql = "SELECT COUNT(*) FROM USER_DATA WHERE USER_ID = ?";

        if (!isExist(sql, friendId) || !isExist(sql, userId)) {
            throw new ValidationException("");
        }

        User user = findOne(userId);

        jdbcTemplate.update("DELETE FROM PUBLIC.FRIEND WHERE USER_ID = ? AND FRIEND_ID = ?", userId, friendId);

        user.removeFriend(friendId);
    }

    private boolean isExist(String sql, long id) {
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class) != 0;
    }
}
