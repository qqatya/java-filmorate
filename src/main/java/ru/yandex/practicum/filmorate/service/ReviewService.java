package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {
    /**
     * Создание отзыва
     *
     * @param review Объект, содержащий данные для создания
     * @return Созданный отзыв
     */
    Review createReview(Review review);

    /**
     * Обновление отзыва
     *
     * @param review Объект, содержащий данные для обновления
     * @return Обновленный отзыв
     */
    Review updateReview(Review review);

    /**
     * Получение отзыва по идентификатору
     *
     * @param id Идентификатор отзыва
     * @return Отзыв
     */
    Review getReviewById(Integer id);

    /**
     * Удаление отзыва по идентификатору
     *
     * @param id Идентификатор отзыва
     */
    void deleteReviewById(Integer id);

    /**
     * Получение всех отзывов
     *
     * @return Список отзывов
     */
    List<Review> getAllReviews();
}
