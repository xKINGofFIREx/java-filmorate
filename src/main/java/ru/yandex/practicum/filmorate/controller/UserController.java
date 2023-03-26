package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private static int ids = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        if (users.containsKey(user.getId()))
            user.setId(++ids);

        if (user.getName().equals(""))
            user.setName(user.getLogin());

        users.put(user.getId(), user);

        return user;
    }


    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {

        if (!users.containsKey(user.getId()))
            throw new ValidationException("пользователя не существует");
        if (user.getName().equals(""))
            user.setName(user.getLogin());

        users.put(user.getId(), user);

        return user;
    }
}
