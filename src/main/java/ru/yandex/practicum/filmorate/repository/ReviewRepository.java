package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    /**
     * Создание отзыва
     *
     * @param review Объект, содержащий данные для создания
     * @return Созданный отзыв
     */
    Review insertReview(Review review);

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
     * @return Отзыв (пустой, если такого отзыва нет)
     */
    Optional<Review> getReviewById(Integer id);

    /**
     * Удаление отзыва по идентификатору
     *
     * @param id Идентификатор отзыва
     */
    void deleteReviewById(Integer id);

    /**
     * Проверка на существование отзыва
     *
     * @param id Идентификатор отзыва
     * @return Признак существования
     */
    boolean doesExist(Integer id);

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
     * @param id Идентификатор отзыва
     * @return Отзыв
     */
    Review increaseUsefulRate(Integer id, Integer userId);

    /**
     * Снижение рейтинга полезности отзыва
     *
     * @param id Идентификатор отзыва
     * @return Отзыв
     */
    Review decreaseUsefulRate(Integer id, Integer userId);

    /**
     * Проверка на существование лайка/дизлайка отзыву от пользователя
     *
     * @param id         Идентификатор отзыва
     * @param userId     Идентификатор пользователя
     * @param isPositive Признак лайк/дизлайк
     * @return Признак существования лайка/дизлайка
     */
    Boolean isRatedByUser(Integer id, Integer userId, boolean isPositive);

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
