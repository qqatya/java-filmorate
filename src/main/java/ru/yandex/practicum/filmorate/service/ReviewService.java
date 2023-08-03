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

    /**
     * Получение отзывов по идентификатору фильма
     *
     * @param id    Идентификатор фильма
     * @param count Количество отзывов
     * @return Список отзывов
     */
    List<Review> getReviewsByFilmId(Integer id, Integer count);

    /**
     * Увеличение рейтинга полезности отзыва
     *
     * @param id     Идентификатор отзыва
     * @param userId Идентификатор пользователя
     * @return Отзыв
     */
    Review increaseUsefulRate(Integer id, Integer userId);

    /**
     * Снижение рейтинга полезности отзыва
     *
     * @param id     Идентификатор отзыва
     * @param userId Идентификатор пользователя
     * @return Отзыв
     */
    Review decreaseUsefulRate(Integer id, Integer userId);

    /**
     * Удаление оценки полезности отзыва
     *
     * @param id         Идентификатор отзыва
     * @param userId     Идентификатор пользователя
     * @param isPositive Признак лайк/дизлайк
     * @return Отзыв
     */
    Review deleteUsefulRate(Integer id, Integer userId, boolean isPositive);

}
