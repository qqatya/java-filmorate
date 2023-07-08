package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {

    /**
     * Получение всех жанров
     *
     * @return Список жанров
     */
    List<Genre> getAllGenres();

    /**
     * Получение жанра по идентификатору
     *
     * @param id Идентификатор жанра
     * @return Жанр
     */
    Genre getGenreById(Integer id);
}
