package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private static int idCount = 1;
    private final Set<User> users = new HashSet();

    @GetMapping
    public Set<User> findAll() {
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {

        if (users.contains(user))
            throw new ValidationException("пользователь уже существует");
        if (user.getEmail().equals("") || !user.getEmail().contains("@"))
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        if (user.getLogin().equals("") || user.getLogin().contains(" "))
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("дата рождения не может быть в будущем");
        if (user.getName().equals(""))
            user.setName(user.getLogin());
        if (user.getId() == 0)
            user.setId(idCount++);
        users.add(user);

        return user;
    }


    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        if (user.getClass() != User.class)
            throw new ValidationException("d");

        if (user.getEmail().equals("") || !user.getEmail().contains("@"))
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        if (user.getLogin().equals("") || user.getLogin().contains(" "))
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("дата рождения не может быть в будущем");
        if (user.getName().equals(""))
            user.setName(user.getLogin());
        if (user.getId() == 0)
            user.setId(idCount++);
        users.add(user);

        return user;
    }
}
