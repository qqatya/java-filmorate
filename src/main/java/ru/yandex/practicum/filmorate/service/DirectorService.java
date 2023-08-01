package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {
    /**
     * Создание режиссёра
     *
     * @param director Объект, содержащий данные для создания
     * @return Созданный пользователь
     */
    Director createDirector(Director director);

    /**
     * Обновление пользователя
     *
     * @param director Объект, содержащий данные для обновления
     * @return Обновленный пользователь
     */
    Director updateDirector(Director director);

    /**
     * Получение всех пользователей
     *
     * @return Список пользователей
     */
    List<Director> getAllDirectors();

    /**
     * Получение пользователя по идентификатору
     *
     * @param id Идентификатор пользователя
     * @return Пользователь
     */
    Director getDirectorById(Integer id);

    /**
     * Удаление режиссера по идентификатору
     *
     * @param id Идентификатор режиссера
     */
    void deleteDirectorById(Integer id);
}