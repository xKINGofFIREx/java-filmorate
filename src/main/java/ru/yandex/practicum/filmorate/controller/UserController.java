package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final Set<User> users = new HashSet<>();

    @GetMapping
    public Set<User> findAll() {
        log.info("Получен запрос GET.");
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос POST.");
        try {
            if (users.contains(user))
                throw new ValidationException("пользователь уже существует");
            if (user.getEmail().isEmpty() || !user.getEmail().contains("@"))
                throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
            if (user.getLogin().isEmpty() || user.getLogin().contains(" "))
                throw new ValidationException("логин не может быть пустым и содержать пробелы");
            if (user.getName().isEmpty())
                user.setName(user.getLogin());
            if (user.getBirthday().isAfter(LocalDate.now()))
                throw new ValidationException("дата рождения не может быть в будущем");

            users.add(user);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получен запрос PUT.");
        try {
            if (user.getEmail().isEmpty() || !user.getEmail().contains("@"))
                throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
            if (user.getLogin().isEmpty() || user.getLogin().contains(" "))
                throw new ValidationException("логин не может быть пустым и содержать пробелы");
            if (user.getName().isEmpty())
                user.setName(user.getLogin());
            if (user.getBirthday().isAfter(LocalDate.now()))
                throw new ValidationException("дата рождения не может быть в будущем");


        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }
}
