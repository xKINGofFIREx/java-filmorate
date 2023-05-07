package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static long ids = 1;
    private final Map<Long, User> users = new HashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        if (users.containsKey(user.getId()))
            user.setId(++ids);

        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(User user) throws NotFoundException {
        if (!users.containsKey(user.getId()))
            throw new NotFoundException("пользователя не существует");

        users.put(user.getId(), user);

        return user;
    }

    @Override
    public void delete(long userId) {
        users.remove(userId);
    }

    public User getUser(long id) throws NotFoundException {
        if (!users.containsKey(id))
            throw new NotFoundException("пользователя не существует");

        return users.get(id);
    }


}
