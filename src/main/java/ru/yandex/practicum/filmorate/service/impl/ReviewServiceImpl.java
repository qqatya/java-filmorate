package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final FilmRepository filmRepository;

    private final UserRepository userRepository;

    @Override
    public Review createReview(Review review) {
        if (!filmRepository.doesExist(review.getFilmId())) {
            throw new FilmNotFoundException(String.valueOf(review.getFilmId()));
        }
        if (!userRepository.doesExist(review.getUserId())) {
            throw new UserNotFoundException(String.valueOf(review.getUserId()));
        }
        return reviewRepository.insertReview(review);
    }

    @Override
    public Review updateReview(Review review) {
        if (reviewRepository.doesExist(review.getReviewId())) {
            if (!filmRepository.doesExist(review.getFilmId())) {
                throw new FilmNotFoundException(String.valueOf(review.getFilmId()));
            }
            if (!userRepository.doesExist(review.getUserId())) {
                throw new UserNotFoundException(String.valueOf(review.getUserId()));
            }

            log.info("Updating reviewId = {}", review.getReviewId());
            return reviewRepository.updateReview(review);
        }
        throw new ReviewNotFoundException(String.valueOf(review.getReviewId()));
    }

    @Override
    public Review getReviewById(Integer id) {
        log.info("Getting reviewId = {}", id);
        return reviewRepository.getReviewById(id)
                .orElseThrow(() -> new ReviewNotFoundException(String.valueOf(id)));
    }

    @Override
    public void deleteReviewById(Integer id) {
        if (!reviewRepository.doesExist(id)) {
            throw new ReviewNotFoundException(String.valueOf(id));
        }
        log.info("Deleting reviewId = {}", id);
        reviewRepository.deleteReviewById(id);
    }

    @Override
    public List<Review> getAllReviews() {
        log.info("Returning all reviews");
        return reviewRepository.getAllReviews();
    }

    @Override
    public List<Review> getReviewsByFilmId(Integer id, Integer count) {
        if (!filmRepository.doesExist(id)) {
            throw new FilmNotFoundException(String.valueOf(id));
        }
        log.info("Getting reviews count = {} by filmId = {}", count, id);
        return reviewRepository.getReviewsByFilmId(id, count);
    }

    @Override
    public Review increaseUseful(Integer id, Integer userId) {
        if (!reviewRepository.doesExist(id)) {
            throw new ReviewNotFoundException(String.valueOf(id));
        }
        if (!userRepository.doesExist(userId)) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        log.info("Increase useful rating for reviewId = {}", id);
        return reviewRepository.increaseUseful(id);
    }

    @Override
    public Review decreaseUseful(Integer id, Integer userId) {
        if (!reviewRepository.doesExist(id)) {
            throw new ReviewNotFoundException(String.valueOf(id));
        }
        if (!userRepository.doesExist(userId)) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        log.info("Decrease useful rating for reviewId = {}", id);
        return reviewRepository.decreaseUseful(id);
    }
}