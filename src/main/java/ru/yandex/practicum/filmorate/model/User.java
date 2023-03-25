package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {

    private int id = 1;

    @Email
    @NotNull
    private String email;

    @NotNull
    @NotBlank
    private String login;

    @NotNull
    private String name = "";

    @NotNull
    @Past
    private LocalDate birthday;
}
