package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> findAll() {
        return ((InMemoryFilmStorage) filmStorage).findAll();
    }

    public Film create(Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws ValidationException {
        return filmStorage.update(film);
    }

    public void delete(long filmId) throws ValidationException {
        if (filmId < 1)
            throw new ValidationException("");
        filmStorage.delete(filmId);
    }

    public void addLike(long filmId, long userId) throws ValidationException {
        if (userId < 1 || filmId < 1)
            throw new ValidationException("");

        Film film = ((InMemoryFilmStorage) filmStorage).getFilm(filmId);

        film.addLike(userId);
    }

    public void removeLike(long filmId, long userId) throws ValidationException {
        if (userId < 1 || filmId < 1)
            throw new ValidationException("");

        Film film = ((InMemoryFilmStorage) filmStorage).getFilm(filmId);

        film.removeLike(userId);
    }

    public List<Film> getTopFilms(int count) {
        if (count == -1)
            return topFilms(10);

        return topFilms(count);
    }

    public Film getFilm(long id) throws ValidationException {
        return ((InMemoryFilmStorage) filmStorage).getFilm(id);
    }

    private List<Film> topFilms(int count) {
        TreeSet<Film> films = new TreeSet<>(Comparator.comparing(Film::getLikeAmount).thenComparing(Film::getId).reversed());
        films.addAll(((InMemoryFilmStorage) filmStorage).findAll());
        List<Film> sortedFilms = new ArrayList<>(films);

        if (count > sortedFilms.size())
            count = sortedFilms.size();

        return sortedFilms.subList(0, count);
    }


}
