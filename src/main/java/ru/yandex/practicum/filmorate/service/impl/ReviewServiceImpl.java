package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.Operation;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

import static ru.yandex.practicum.filmorate.model.type.ExceptionType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final FilmRepository filmRepository;

    private final UserRepository userRepository;

    private final EventService eventService;

    @Override
    public Review createReview(Review review) {
        if (!filmRepository.doesExist(review.getFilmId())) {
            throw new NotFoundException(FILM_NOT_FOUND.getValue() + review.getFilmId());
        }
        if (!userRepository.doesExist(review.getUserId())) {
            throw new NotFoundException(USER_NOT_FOUND.getValue() + review.getUserId());
        }
        Review createdReview = reviewRepository.insertReview(review);
        Event event = eventService.addEvent(new Event(createdReview.getUserId(), EventType.REVIEW, Operation.ADD,
                createdReview.getReviewId()));

        log.info("Created eventId = {}", event.getEventId());
        return createdReview;
    }

    @Override
    public Review updateReview(Review review) {
        if (reviewRepository.doesExist(review.getReviewId())) {
            if (!filmRepository.doesExist(review.getFilmId())) {
                throw new NotFoundException(FILM_NOT_FOUND.getValue() + review.getFilmId());
            }
            if (!userRepository.doesExist(review.getUserId())) {
                throw new NotFoundException(USER_NOT_FOUND.getValue() + review.getUserId());
            }
            log.info("Updating reviewId = {}", review.getReviewId());
            Review updatedReview = reviewRepository.updateReview(review);
            Event event = eventService.addEvent(new Event(updatedReview.getUserId(), EventType.REVIEW, Operation.UPDATE,
                    updatedReview.getReviewId()));

            log.info("Created eventId = {}", event.getEventId());
            return updatedReview;
        }
        throw new NotFoundException(REVIEW_NOT_FOUND.getValue() + review.getReviewId());
    }

    @Override
    public Review getReviewById(Integer id) {
        log.info("Getting reviewId = {}", id);
        return reviewRepository.getReviewById(id)
                .orElseThrow(() -> new NotFoundException(REVIEW_NOT_FOUND.getValue() + id));
    }

    @Override
    public void deleteReviewById(Integer id) {
        if (!reviewRepository.doesExist(id)) {
            throw new NotFoundException(REVIEW_NOT_FOUND.getValue() + id);
        }
        Review review = reviewRepository.getReviewById(id)
                .orElseThrow(() -> new NotFoundException(REVIEW_NOT_FOUND.getValue() + id));

        log.info("Deleting reviewId = {}", id);
        reviewRepository.deleteReviewById(id);
        Event event = eventService.addEvent(new Event(review.getUserId(), EventType.REVIEW, Operation.REMOVE,
                review.getReviewId()));

        log.info("Created eventId = {}", event.getEventId());
    }

    @Override
    public List<Review> getAllReviews() {
        log.info("Returning all reviews");
        return reviewRepository.getAllReviews();
    }

    @Override
    public List<Review> getReviewsByFilmId(Integer id, Integer count) {
        if (!filmRepository.doesExist(id)) {
            throw new NotFoundException(FILM_NOT_FOUND.getValue() + id);
        }
        log.info("Getting reviews count = {} by filmId = {}", count, id);
        return reviewRepository.getReviewsByFilmId(id, count);
    }

    @Override
    public Review increaseUsefulRate(Integer id, Integer userId) {
        if (!reviewRepository.doesExist(id)) {
            throw new NotFoundException(REVIEW_NOT_FOUND.getValue() + id);
        }
        if (!userRepository.doesExist(userId)) {
            throw new NotFoundException(USER_NOT_FOUND.getValue() + userId);
        }
        if (reviewRepository.isRatedByUser(id, userId, true)) {
            throw new ReviewAlreadyRatedException(String.valueOf(id));
        }
        log.info("Increase useful rating for reviewId = {}", id);
        return reviewRepository.increaseUsefulRate(id, userId);
    }

    @Override
    public Review decreaseUsefulRate(Integer id, Integer userId) {
        if (!reviewRepository.doesExist(id)) {
            throw new NotFoundException(REVIEW_NOT_FOUND.getValue() + id);
        }
        if (!userRepository.doesExist(userId)) {
            throw new NotFoundException(USER_NOT_FOUND.getValue() + userId);
        }
        if (reviewRepository.isRatedByUser(id, userId, false)) {
            throw new ReviewAlreadyRatedException(String.valueOf(id));
        }
        log.info("Decrease useful rating for reviewId = {}", id);
        return reviewRepository.decreaseUsefulRate(id, userId);
    }

    @Override
    public Review deleteUsefulRate(Integer id, Integer userId, boolean isPositive) {
        if (!reviewRepository.doesExist(id)) {
            throw new NotFoundException(REVIEW_NOT_FOUND.getValue() + id);
        }
        if (!userRepository.doesExist(userId)) {
            throw new NotFoundException(USER_NOT_FOUND.getValue() + userId);
        }
        if (!reviewRepository.isRatedByUser(id, userId, isPositive)) {
            throw new NotFoundException(REVIEW_RATE_NOT_FOUND.getValue() + userId);
        }
        log.info("Deleting rate for reviewId = {} from userId = {}", id, userId);
        return reviewRepository.deleteUsefulRate(id, userId, isPositive);
    }
}
