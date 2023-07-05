package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.RatingRepository;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Override
    public List<Rating> getAllRatings() {
        List<Rating> ratings = ratingRepository.getAllRatings();

        log.info("Found {} ratings", ratings.size());
        return ratings;
    }

    @Override
    public Rating getRatingById(Integer id) {
        log.info("Getting rating by id = {}", id);
        return ratingRepository.getRatingById(id)
                .orElseThrow(() -> new RatingNotFoundException(String.valueOf(id)));
    }
}
