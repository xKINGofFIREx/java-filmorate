package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

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
        return ((FilmDbStorage) filmStorage).findAll();
    }

    public Film create(Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws NotFoundException {
        return filmStorage.update(film);
    }

    public void delete(long filmId) throws NotFoundException {
        if (filmId < 1)
            throw new NotFoundException();
        filmStorage.delete(filmId);
    }

    public void addLike(long filmId, long userId) throws NotFoundException {
        if (userId < 1 || filmId < 1)
            throw new NotFoundException();

        Film film = ((FilmDbStorage) filmStorage).getFilm(filmId);

        film.addLike(userId);
    }

    public void removeLike(long filmId, long userId) throws NotFoundException {
        if (userId < 1 || filmId < 1)
            throw new NotFoundException();

        Film film = ((FilmDbStorage) filmStorage).getFilm(filmId);

        film.removeLike(userId);
    }

    public List<Film> getTopFilms(int count) {
        if (count == -1)
            return topFilms(10);

        return topFilms(count);
    }

    public Film getFilm(long id) throws NotFoundException {
        return ((FilmDbStorage) filmStorage).getFilm(id);
    }

    private List<Film> topFilms(int count) {
        TreeSet<Film> films = new TreeSet<>(Comparator.comparing(Film::getLikeAmount).thenComparing(Film::getId).reversed());
        films.addAll(((FilmDbStorage) filmStorage).findAll());
        List<Film> sortedFilms = new ArrayList<>(films);

        if (count > sortedFilms.size())
            count = sortedFilms.size();

        return sortedFilms.subList(0, count);
    }

    public List<Genre> getGenreList() {
        return ((FilmDbStorage) filmStorage).getGenreList();
    }

    public Genre getGenreById(long genreId) throws NotFoundException {
        return ((FilmDbStorage) filmStorage).getGenreById(genreId);
    }

    public List<MPA> getMPAList() {
        return ((FilmDbStorage) filmStorage).getMPAList();
    }

    public MPA getMPAById(long mpaId) throws NotFoundException {
        if (mpaId > 5)
            throw new NotFoundException();
        return ((FilmDbStorage) filmStorage).getMPAById(mpaId);
    }


}
