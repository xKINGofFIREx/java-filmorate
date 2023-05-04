package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class Film {

    private final Set<Long> likes = new HashSet<>();
    private long id;
    private final Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
    private MPA mpa;
    private int rate;

    @NotNull
    @NotEmpty
    private String name = "common";

    @NotNull
    @NotBlank
    @Size(max = 200)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive
    @NotNull
    private int duration;

    public void setGenres(List<Genre> genres) {
        this.genres.addAll(genres);
    }

    public void setLikes(List<Long> likes) {
        this.likes.addAll(likes);
    }

    public void addLike(long userId) {
        likes.add(userId);
    }

    public void removeLike(long userId) {
        likes.remove(userId);
    }

    public int getLikeAmount() {
        return likes != null ? likes.size() : 0;
    }
}
