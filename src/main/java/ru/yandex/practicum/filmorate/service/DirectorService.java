package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {
    /**
     * Создание режиссёра
     *
     * @param director Объект, содержащий данные для создания
     * @return Созданный режиссёр
     */
    Director createDirector(Director director);

    /**
     * Обновление режиссёра
     *
     * @param director Объект, содержащий данные для обновления
     * @return Обновленный режиссёр
     */
    Director updateDirector(Director director);

    /**
     * Получение всех режиссёров
     *
     * @return Список режиссёров
     */
    List<Director> getAllDirectors();

    /**
     * Получение режиссёра по идентификатору
     *
     * @param id Идентификатор режиссёра
     * @return режиссёр
     */
    Director getDirectorById(Integer id);

    /**
     * Удаление режиссера по идентификатору
     *
     * @param id Идентификатор режиссера
     */
    void deleteDirectorById(Integer id);
}