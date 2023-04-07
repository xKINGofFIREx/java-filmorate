package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static long ids = 1;
    private final Map<Long, Film> films = new HashMap<>();

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("дата релиза — не может быть раньше 28.12.1985");

        if (films.containsKey(film.getId()))
            film.setId(++ids);

        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        if (!films.containsKey(film.getId()))
            throw new ValidationException("фильма не существует");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("дата релиза — не может быть раньше 28.12.1985");

        films.put(film.getId(), film);

        return film;
    }

    @Override
    public void delete(long filmId) {
        films.remove(filmId);
    }

    public Film getFilm(long id) throws ValidationException {
        if (!films.containsKey(id))
            throw new ValidationException("");
        return films.get(id);
    }
}
