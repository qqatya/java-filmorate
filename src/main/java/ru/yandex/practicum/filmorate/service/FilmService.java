package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    /**
     * Создание фильма
     *
     * @param film Объект, содержащий данные для создания
     * @return Созданный фильм
     */
    Film createFilm(Film film);

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
     * @return Фильм
     */
    Film getFilmById(Integer id);

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
     * Получение фильмов по маскимальному количеству лайков
     *
     * @param count Количество фильмов
     * @return Список фильмов
     */
    List<Film> getPopularFilms(Integer count);

    /**
     * Удаление фильма по идентификатору
     *
     * @param id Идентификатор фильма
     */
    void deleteFilmById(Integer id);
}
