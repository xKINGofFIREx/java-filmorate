package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return ((InMemoryUserStorage) userStorage).findAll();
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
        User user = ((InMemoryUserStorage) userStorage).getUser(userId);
        User friend = ((InMemoryUserStorage) userStorage).getUser(friendId);

        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(long userId, long friendId) throws ValidationException {
        User user = ((InMemoryUserStorage) userStorage).getUser(userId);
        User friend = ((InMemoryUserStorage) userStorage).getUser(friendId);

        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public List<User> getFriends(long userId) throws ValidationException {
        User user = ((InMemoryUserStorage) userStorage).getUser(userId);
        List<Long> friendsIds = new ArrayList<>(user.getFriends());
        List<User> friends = new ArrayList<>();

        for (long id : friendsIds) {
            friends.add(((InMemoryUserStorage) userStorage).getUser(id));
        }

        return friends;
    }

    public List<User> getCommonFriends(long userId, long otherId) throws ValidationException {
        User user = ((InMemoryUserStorage) userStorage).getUser(userId);
        User friend = ((InMemoryUserStorage) userStorage).getUser(otherId);

        List<User> commonFriends = new ArrayList<>();

        for (long id : user.getFriends()) {
            if (friend.getFriends().contains(id)) {
                commonFriends.add(((InMemoryUserStorage) userStorage).getUser(id));
            }
        }

        return commonFriends;
    }

    public User getUserById(long userId) throws ValidationException {
        return ((InMemoryUserStorage) userStorage).getUser(userId);
    }
}
