package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {

    /**
     * Получение жанров по идентификатору фильма
     *
     * @param id Идентификатор фильма
     * @return Множество жанров
     */
    Set<Genre> getByFilmId(Integer id);


    /**
     * Присвоение жанров фильму
     *
     * @param filmId Идентификатор фильма
     * @param genres Множество жанров
     * @return Множество присвоенных жанров
     */
    Set<Genre> insertFilmGenres(Set<Genre> genres, Integer filmId);

    /**
     * Обновление жанров фильма
     *
     * @param filmId Идентификатор фильма
     * @param genres Множество жанров
     * @return Множество обновленных жанров
     */
    Set<Genre> updateFilmGenres(Set<Genre> genres, Integer filmId);

    /**
     * Удаление всех жанров фильма
     *
     * @param filmId Идентификатор фильма
     */
    void deleteFilmGenres(Integer filmId);

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
     * @return Жанр (пустой, если не найден
     */
    Optional<Genre> getGenreById(Integer id);

}
