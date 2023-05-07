package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

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
    public Film create(Film film) {

        if (films.containsKey(film.getId()))
            film.setId(++ids);

        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film update(Film film) throws NotFoundException {
        if (!films.containsKey(film.getId()))
            throw new NotFoundException("фильма не существует");

        films.put(film.getId(), film);

        return film;
    }

    @Override
    public void delete(long filmId) {
        films.remove(filmId);
    }

    public Film getFilm(long id) throws NotFoundException {
        if (!films.containsKey(id))
            throw new NotFoundException("фильма не существует");
        return films.get(id);
    }
}
