package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventRepository {

    /**
     * Создание события
     *
     * @param event Объект, содержащий данные для создания
     * @return Созданное событие
     */
    Event insertEvent(Event event);

    /**
     * Получение ленты событий по идентификатору пользователя
     *
     * @param id Идентификатор пользователя
     * @return Список событий
     */
    List<Event> getEventsByUserId(Integer id);

}