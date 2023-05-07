package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User create(User user);

    User update(User user) throws NotFoundException;

    void delete(long userId);
}
