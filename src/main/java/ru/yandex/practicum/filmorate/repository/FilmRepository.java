package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    /**
     * Создание/обновление фильма
     *
     * @param film Объект, содержащий данные для создания
     * @return Созданный фильм
     */
    Film insertFilm(Film film);

    /**
     * Обновление фильма
     *
     * @param film Объект, содержащий данные для обновления
     * @return Обновленный фильм
     */
    Film updateFilm(Film film);

    /**
     * Получение всех фильмов
     *
     * @return Список фильмов
     */
    List<Film> getAllFilms();

    /**
     * Получение фильма по идентификатору
     *
     * @param id Идентификатор фильма
     * @return Фильм (пустой, если такого фильма нет)
     */
    Optional<Film> getFilmById(Integer id);

    /**
     * Добавление лайка пользователя
     *
     * @param id     Идентификатор фильма
     * @param userId Идентификатор пользователя
     * @return Фильм с обновленным списком лайков
     */
    Film putLike(Integer id, Integer userId);

    /**
     * Удаление лайка пользователя
     *
     * @param id     Идентификатор фильма
     * @param userId Идентификатор пользователя
     * @return Фильм с обновленным списком лайков
     */
    Film deleteLike(Integer id, Integer userId);

    /**
     * Добавить возможность выводить топ-N по количеству фильмов лайков
     * Фильтрация должна быть по заданным параметрам
     * 1.По жанру.
     * 2.За указанный год.
     *
     * @param count   Количество фильмов
     * @param genreId Идентификатор жанра
     * @param year    год выпуска
     * @return Список фильмов
     */
    List<Film> getPopularFilms(Integer count, Integer genreId, Integer year);

    /**
     * Проверка на существование фильма
     *
     * @param id Идентификатор фильма
     * @return Признак существования
     */
    boolean doesExist(Integer id);

    /**
     * Вывод общих с другом фильмов с сортировкой по их популярности
     *
     * @param userId   Идентификатор пользователя, запрашивающего информацию
     * @param friendId Идентификатор пользователя, с которым необходимо сравнить список фильмов
     * @return Список фильмов, отсортированных по популярности
     */
    List<Film> getCommonFilms(Integer userId, Integer friendId);

    /**
     * Удаление фильма по идентификатору
     *
     * @param id Идентификатор фильма
     */
    void deleteFilmById(Integer id);

}
