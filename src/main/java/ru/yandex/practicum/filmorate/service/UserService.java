package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return ((UserDbStorage) userStorage).findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) throws ValidationException {
        return userStorage.update(user);
    }

    public void delete(long userId) {
        userStorage.delete(userId);
    }

    public void addFriend(long userId, long friendId) throws ValidationException {
        ((UserDbStorage) userStorage).addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) throws ValidationException {
        ((UserDbStorage) userStorage).removeFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) throws ValidationException {
        User user = getUserById(userId);
        List<Long> friendsIds = new ArrayList<>(user.getFriends());
        List<User> friends = new ArrayList<>();

        for (long id : friendsIds) {
            friends.add(getUserById(id));
        }

        return friends;
    }

    public List<User> getCommonFriends(long userId, long otherId) throws ValidationException {
        User user = getUserById(userId);
        User friend = getUserById(otherId);

        List<User> commonFriends = new ArrayList<>();

        for (long id : user.getFriends()) {
            if (friend.getFriends().contains(id)) {
                commonFriends.add(getUserById(id));
            }
        }

        return commonFriends;
    }

    public User getUserById(long userId) throws ValidationException {
        return ((UserDbStorage) userStorage).getUser(userId);
    }
}
