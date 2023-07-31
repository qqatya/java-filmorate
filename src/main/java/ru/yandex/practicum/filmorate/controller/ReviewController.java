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
     * Получение всех отзывов
     *
     * @return Список отзывов
     */
    @GetMapping
    public List<Review> getAllFilms() {
        return reviewService.getAllReviews();
    }
}
