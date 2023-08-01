package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {

    private Integer id;

    @NonNull
    private String name;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    /**
     * Продолжительность фильма в минутах
     */
    private Long duration;

    private Set<Integer> usersLiked;

    private Set<Genre> genres;

    private Set<Director> directors;

    private Rating mpa;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Long duration,
                Set<Integer> usersLiked, Set<Genre> genres, Set<Director> directors, Rating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.usersLiked = usersLiked == null ? new HashSet<>() : usersLiked;
        this.genres = genres == null ? new HashSet<>() : genres;
        this.directors = directors == null ? new HashSet<>() : directors;
        this.mpa = mpa;
    }
}
