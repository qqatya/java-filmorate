package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    /**
     * Получение всех рейтингов
     *
     * @return Список рейтингов
     */
    @GetMapping
    public List<Rating> getAllRatings() {
        return ratingService.getAllRatings();
    }

    /**
     * Получение рейтинга по идентификатору
     *
     * @param id Идентификатор рейтинга
     * @return Рейтинг
     */
    @GetMapping("/{id}")
    public Rating getRatingById(@PathVariable Integer id) {
        return ratingService.getRatingById(id);
    }
}
