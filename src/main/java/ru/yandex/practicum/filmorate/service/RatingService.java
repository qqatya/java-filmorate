package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingService {

    /**
     * Получение всех рейтингов
     *
     * @return Список рейтингов
     */
    List<Rating> getAllRatings();

    /**
     * Получение рейтинга по идентификатору
     *
     * @param id Идентификатор рейтинга
     * @return Рейтинг
     */
    Rating getRatingById(Integer id);
}
