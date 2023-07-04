package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreRepository {
    Set<Genre> getByFilmId(Integer id);

    Set<Genre> insertFilmGenres(Set<Genre> genres, Integer filmId);

    Set<Genre> updateFilmGenres(Set<Genre> genres, Integer filmId);

    void deleteFilmGenres(Integer filmId);

}
