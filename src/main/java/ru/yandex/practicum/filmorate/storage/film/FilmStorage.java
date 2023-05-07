package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    Film create(Film film) throws ValidationException;

    Film update(Film film) throws NotFoundException;

    void delete(long filmId);
}
