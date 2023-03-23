package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static int idCount = 1;
    private final Set<Film> films = new HashSet<>();

    @GetMapping
    public Set<Film> findAll() {
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {

        if (films.contains(film))
            throw new ValidationException("фильм уже существует");
        if (film.getName().equals(""))
            throw new ValidationException("название не может быть пустым");
        if (film.getDescription().length() > 200)
            throw new ValidationException("максимальная длина описания — 200 символов");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("дата релиза — не может быть раньше 28.12.1985");
        if (film.getDuration() < 1)
            throw new ValidationException("продолжительность фильма должна быть положительной");
        if (film.getId() == 0)
            film.setId(idCount++);
        films.add(film);

        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {

        if (film.getName().equals(""))
            throw new ValidationException("название не может быть пустым");
        if (film.getDescription().length() > 200)
            throw new ValidationException("максимальная длина описания — 200 символов");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("дата релиза — не может быть раньше 28.12.1985");
        if (film.getDuration() < 1)
            throw new ValidationException("продолжительность фильма должна быть положительной");
        if (film.getId() == 0)
            film.setId(idCount++);
        films.add(film);

        return film;
    }
}
