package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DirectorRepository {
    /**
     * Создание режиссера
     *
     * @param director Объект, содержащий данные для создания
     * @return Созданный режиссер
     */
    Director insertDirector(Director director);

    /**
     * Обновление режиссера
     *
     * @param director Объект, содержащий данные для обновления
     * @return Обновленный режиссер
     */
    Director updateDirector(Director director);

    /**
     * Получение всех режиссеров
     *
     * @return Список режиссеров
     */
    List<Director> getAllDirectors();

    /**
     * Получение режиссера по идентификатору
     *
     * @param id Идентификатор режиссера
     * @return режиссер (пустой, если такого режиссёра нет)
     */
    Optional<Director> getDirectorById(Integer id);

    /**
     * Удаление режиссера по идентификатору
     *
     * @param id Идентификатор режиссера
     */
    void deleteDirectorById(Integer id);

    /**
     * Проверка на существование режиссера
     *
     * @param id Идентификатор режиссера
     * @return Признак существования
     */
    boolean doesExist(Integer id);

    /**
     * Присвоение режиссеров фильму
     *
     * @param filmId    Идентификатор фильма
     * @param directors Множество режиссеров
     * @return Множество присвоенных режиссеров
     */
    Set<Director> insertFilmDirectors(Set<Director> directors, Integer filmId);

    /**
     * Обновление режиссеров фильма
     *
     * @param filmId    Идентификатор фильма
     * @param directors Множество режиссеров
     * @return Множество обновленных режиссеров
     */
    Set<Director> updateFilmDirectors(Set<Director> directors, Integer filmId);

    /**
     * Удаление всех режиссеров фильма
     *
     * @param filmId Идентификатор фильма
     */
    void deleteFilmDirectors(Integer filmId);

    /**
     * Получение режиссеров по идентификатору фильма
     *
     * @param id Идентификатор фильма
     * @return Множество режиссеров
     */
    Set<Director> getByFilmId(Integer id);
}