package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private static long ids = 1;
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO FILM(FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES(?, ?, ?, ?, ?, ?)";

        film.setId(ids++);
        jdbcTemplate.update(sql,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        if (film.getGenres() != null) {

            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO PUBLIC.GENRE_LIST(FILM_ID, GENRE_ID) VALUES (?, ?)", film.getId(), genre.getId());
            }
        }

        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        String sql = "UPDATE PUBLIC.FILM SET " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                "WHERE FILM_ID = ?";

        String exist = "SELECT COUNT(*) FROM PUBLIC.FILM WHERE FILM_ID = ?";

        if (!isExist(exist, film.getId())) {
            throw new ValidationException("");
        }

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres() != null) {
            jdbcTemplate.update("DELETE FROM PUBLIC.GENRE_LIST WHERE FILM_ID = ?", film.getId());
            for (Genre genre : film.getGenres()) {
                if (jdbcTemplate.queryForObject("SELECT COUNT(*) FROM GENRE_LIST WHERE FILM_ID = ? AND GENRE_ID = ?",
                        new Object[]{film.getId(), genre.getId()}, Integer.class) == 0) {
                    jdbcTemplate.update("INSERT INTO PUBLIC.GENRE_LIST(FILM_ID, GENRE_ID) VALUES (?, ?)", film.getId(), genre.getId());
                }
            }
        }

        return film;
    }

    public List<Film> findAll() {
        String sql = "SELECT COUNT(*) FROM PUBLIC.FILM WHERE FILM_ID = ?";
        List<Film> films = new ArrayList<>();

        for (long i = 1; i <= ids; i++) {
            if (isExist(sql, i)) {
                films.add(findOne(i));
            }
        }
        return films;
    }

    public Film findOne(long filmId) {
        String sql = "SELECT FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID " +
                "FROM PUBLIC.FILM WHERE FILM_ID = ?";

        Film film = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, filmId);

        List<Long> likes = jdbcTemplate.query("SELECT * FROM PUBLIC.LIKES WHERE FILM_ID = ?",
                (rs, rowNum) -> rs.getLong("USER_ID"), filmId);

        List<Genre> genres = jdbcTemplate.query("SELECT * FROM PUBLIC.GENRE_LIST JOIN GENRE ON GENRE.GENRE_ID = GENRE_LIST.GENRE_ID WHERE FILM_ID = ?",
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")), filmId);

        if (film != null) {
            film.setLikes(likes);
            film.setGenres(genres);
        }

        return film;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(getMPAById(resultSet.getInt("MPA_ID")))
                .build();
    }

    public Film getFilm(long id) throws ValidationException {
        String sql = "SELECT COUNT(*) FROM PUBLIC.FILM WHERE FILM_ID = ?";

        if (!isExist(sql, id)) {
            throw new ValidationException("");
        }

        return findOne(id);
    }

    @Override
    public void delete(long filmId) {
        String sql = "DELETE FROM PUBLIC.FILM WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);

        jdbcTemplate.update("DELETE FROM PUBLIC.GENRE_LIST WHERE FILM_ID = ?", filmId);
    }

    private boolean isExist(String sql, long id) {
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class) != 0;
    }

    public List<Genre> getGenreList() {
        return jdbcTemplate.query("SELECT * FROM PUBLIC.GENRE",
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")));
    }

    public Genre getGenreById(long genreId) throws ValidationException {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM PUBLIC.GENRE WHERE GENRE_ID = ?",
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")), genreId);
        if (genres.size() == 0) {
            throw new ValidationException("");
        }
        return genres.get(0);
    }

    public List<MPA> getMPAList() {
        return jdbcTemplate.query("SELECT * FROM PUBLIC.MPA",
                (rs, rowNum) -> new MPA(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));
    }

    public MPA getMPAById(long mpaId) {
        return jdbcTemplate.query("SELECT * FROM PUBLIC.MPA WHERE MPA_ID = ?",
                (rs, rowNum) -> new MPA(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")), mpaId).get(0);
    }
}
