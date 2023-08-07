package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventService {

    /**
     * Получение ленты событий по идентификатору пользователя
     *
     * @param id Идентификатор пользователя
     * @return Список событий
     */
    List<Event> getFeedByUserId(Integer id);

    /**
     * Создание события
     *
     * @param event Объект, содержащий данные для создания
     * @return Созданное событие
     */
    Event addEvent(Event event);
}
