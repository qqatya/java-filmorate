package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingRepository {

    /**
     * Получение рейтинга по идентификатору фильма
     *
     * @param id Идентификатор фильма
     * @return Рейтинг (пустой, если не найден)
     */
    Optional<Rating> getByFilmId(Integer id);

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
     * @return Рейтинг (пустой, если не найден)
     */
    Optional<Rating> getRatingById(Integer id);
}