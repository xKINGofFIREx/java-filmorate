package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {

    private final Set<Long> friends = new HashSet<>();
    private long id;
    private final Map<Long, Boolean> friendshipStatus = new HashMap<>();

    @Email
    @NotNull
    private String email;

    @NotNull
    @NotBlank
    private String login;

    @NotNull
    private String name;

    @NotNull
    @Past
    private LocalDate birthday;

    public void addFriend(long userId) {
        friends.add(userId);
    }

    public void removeFriend(long friendId) {
        friends.remove(friendId);
    }
}
