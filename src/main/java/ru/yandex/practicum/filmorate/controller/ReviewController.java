package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Создание отзыва
     *
     * @param review Объект, содержащий данные для создания
     * @return Созданный отзыв
     */
    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) {
        return reviewService.createReview(review);
    }

    /**
     * Обновление отзыва
     *
     * @param review Объект, содержащий данные для обновления
     * @return Обновленный отзыв
     */
    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    /**
     * Получение отзыва по идентификатору
     *
     * @param id Идентификатор отзыва
     * @return Отзыв
     */
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Integer id) {
        return reviewService.getReviewById(id);
    }

    /**
     * Удаление отзыва по идентификатору
     *
     * @param id Идентификатор отзыва
     */
    @DeleteMapping("/{id}")
    public void deleteReviewById(@PathVariable Integer id) {
        reviewService.deleteReviewById(id);
    }

    /**
     * Получение отзывов
     *
     * @param filmId Идентификатор фильма
     * @param count  Количество отзывов
     * @return Список отзывов
     */
    @GetMapping
    public List<Review> getAllReviews(@RequestParam(required = false) Integer filmId,
                                      @RequestParam(defaultValue = "10") Integer count) {
        if (filmId == null) {
            return reviewService.getAllReviews();
        } else {
            return reviewService.getReviewsByFilmId(filmId, count);
        }

    }

    /**
     * Добавление лайка отзыву
     *
     * @param id     Идентификатор отзыва
     * @param userId Идентификатор пользователя
     * @return Отзыв
     */
    @PutMapping("/{id}/like/{userId}")
    public Review likeReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.increaseUsefulRate(id, userId);
    }

    /**
     * Добавление дизлайка отзыву
     *
     * @param id     Идентификатор отзыва
     * @param userId Идентификатор пользователя
     * @return Отзыв
     */
    @PutMapping("/{id}/dislike/{userId}")
    public Review dislikeReview(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.decreaseUsefulRate(id, userId);
    }

    /**
     * Удаление лайка у отзыва
     *
     * @param id     Идентификатор отзыва
     * @param userId Идентификатор пользователя
     * @return Отзыв
     */
    @DeleteMapping("/{id}/like/{userId}")
    public Review deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.deleteUsefulRate(id, userId, true);
    }

    /**
     * Удаление дизлайка у отзыва
     *
     * @param id     Идентификатор отзыва
     * @param userId Идентификатор пользователя
     * @return Отзыв
     */
    @DeleteMapping("/{id}/dislike/{userId}")
    public Review deleteDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        return reviewService.deleteUsefulRate(id, userId, false);
    }
}
