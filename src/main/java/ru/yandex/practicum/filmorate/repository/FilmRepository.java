package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    /**
     * Создание фильма
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
     * Получение топ-N фильмов по количеству лайков с возможностью фильтрации по жанру и году выпуска
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

    /**
     * Получение списка фильмов по идентификатору режиссёра с сортировкой
     *
     * @param directorId Идентификатор режиссёра
     * @param sortBy     Параметр сортировки
     * @return Список фильмов (пустой, если такого режиссёра нет)
     */
    List<Film> getFilmsByDirectorId(Integer directorId, String sortBy);

    /**
     * Получение списка фильмов по парамметрам поиска
     *
     * @param query Текст для поиска
     * @param by    Параметр поиска: по режиссёру/по названию/по режиссеру и по названию
     * @return Список фильмов, отсортированных по популярности
     */
    List<Film> searchFilms(String query, String by);
}