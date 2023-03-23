package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id = 1;
    private String name = "common";
    private String description;
    private LocalDate releaseDate;
    private int duration;

}
