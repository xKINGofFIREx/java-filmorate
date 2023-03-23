package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    private final Set<Film> films = new HashSet<>();

    @GetMapping
    public Set<Film> findAll() {
        log.info("Получен запрос GET.");
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос POST.");
        try {
            if (films.contains(film))
                throw new ValidationException("фильм уже существует");
            if (film.getName().isEmpty())
                throw new ValidationException("название не может быть пустым");
            if (film.getDescription().length() > 200)
                throw new ValidationException("максимальная длина описания — 200 символов");
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
                throw new ValidationException("дата релиза — не может быть раньше 28.12.1985");
            if (film.getDuration() < 1)
                throw new ValidationException("продолжительность фильма должна быть положительной");

            films.add(film);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен запрос PUT.");
        try {
            if (film.getName().isEmpty()) throw new ValidationException("название не может быть пустым");
            if (film.getDescription().length() > 200)
                throw new ValidationException("максимальная длина описания — 200 символов");
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
                throw new ValidationException("дата релиза — не может быть раньше 28.12.1985");
            if (film.getDuration() < 1)
                throw new ValidationException("продолжительность фильма должна быть положительной");

            films.add(film);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return film;
    }
}
