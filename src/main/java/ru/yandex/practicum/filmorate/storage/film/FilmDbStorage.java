package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) throws ValidationException {
        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        return film;
    }

    @Override
    public void delete(long filmId) {
        String sql = "DELETE FROM PUBLIC.FILM WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
