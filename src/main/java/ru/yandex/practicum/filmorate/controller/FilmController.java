package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("дата релиза — не может быть раньше 28.12.1895");

        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("дата релиза — не может быть раньше 28.12.1985");

        try {
            filmService.update(film);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return film;
    }

    @GetMapping("/films/{filmId}")
    public Film getFilm(@PathVariable long filmId) {
        Film film;
        try {
            film = filmService.getFilm(filmId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return film;
    }

    @DeleteMapping("/films/{filmId}")
    public void delete(@PathVariable long filmId) throws NotFoundException {
        filmService.delete(filmId);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        try {
            filmService.addLike(filmId, userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
        try {
            filmService.removeLike(filmId, userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam Optional<Integer> count) {
        return filmService.getTopFilms(count.orElse(-1));
    }

    @GetMapping("/genres")
    public List<Genre> getGenreList() {
        return filmService.getGenreList();
    }

    @GetMapping("/genres/{genreId}")
    public Genre getGenreById(@PathVariable int genreId) {
        Genre genre;
        try {
            genre = filmService.getGenreById(genreId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return genre;
    }

    @GetMapping("/mpa")
    public List<MPA> getMPAList() {
        return filmService.getMPAList();
    }

    @GetMapping("/mpa/{mpaId}")
    public MPA getMPAById(@PathVariable int mpaId) {
        MPA mpa;
        try {
            mpa = filmService.getMPAById(mpaId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return mpa;
    }

}
