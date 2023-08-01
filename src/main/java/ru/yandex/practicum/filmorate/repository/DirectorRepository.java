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
     * @return режиссер (пустой, если такого пользователя нет)
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
    Set<Director> insertFilmDirectors(Set<Director> directors, Integer filmId);
    Set<Director> updateFilmDirectors(Set<Director> directors, Integer filmId);
    void deleteFilmDirectors(Integer filmId);

    Set<Director> getByFilmId(Integer id);
}
