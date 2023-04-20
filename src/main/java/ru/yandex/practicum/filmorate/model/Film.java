package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Set<Long> likes = new HashSet<>();
    private long id = 1;

    private Set<String> genre;
    private MPA mpa;

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
    private int duration;

    public void addLike(long userId) {
        likes.add(userId);
    }

    public void removeLike(long userId) {
        likes.remove(userId);
    }

    public long getLikes() {
        return likes.size();
    }

    public Set<Long> getLike() {
        return likes;
    }
}
