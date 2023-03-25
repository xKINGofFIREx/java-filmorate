package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id = 0;

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
}
