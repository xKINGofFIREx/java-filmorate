package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("дата релиза — не может быть раньше 28.12.1985");

        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("дата релиза — не может быть раньше 28.12.1985");

        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable long filmId) {
        Film film;
        try {
            film = filmService.getFilm(filmId);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }
        return film;
    }

    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable long filmId) throws ValidationException {
        filmService.delete(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        try {
            filmService.addLike(filmId, userId);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }

    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
        try {
            filmService.removeLike(filmId, userId);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }

    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam Optional<Integer> count) {
        return filmService.getTopFilms(count.orElse(-1));
    }

}
