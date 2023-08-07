package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final ReviewMapper reviewMapper;

    private static final String SQL_INSERT_REVIEW = "INSERT INTO public.review "
            + "(content, is_positive, user_id, film_id, useful) VALUES(:content, :is_positive, :user_id, "
            + ":film_id, :useful)";

    private static final String SQL_UPDATE_REVIEW = "UPDATE public.review SET content = :content, "
            + "is_positive = :is_positive "
            + "WHERE id = :id";

    private static final String SQL_GET_REVIEW_BY_ID = "SELECT id, content, is_positive, user_id, film_id, useful "
            + "FROM public.review WHERE id = :id";

    private static final String SQL_DELETE_REVIEW_BY_ID = "DELETE FROM public.review WHERE id = :id";

    private static final String SQL_GET_ALL_REVIEWS = "SELECT id, content, is_positive, user_id, film_id, useful "
            + "FROM public.review ORDER BY useful DESC";

    private static final String SQL_GET_REVIEWS_BY_FILM_ID = "SELECT id, content, is_positive, user_id, film_id, useful "
            + "FROM public.review WHERE film_id = :id ORDER BY useful DESC LIMIT :count";

    private static final String SQL_LIKE_REVIEW = "UPDATE public.review SET useful = useful + 1 "
            + "WHERE id = :id";

    private static final String SQL_DISLIKE_REVIEW = "UPDATE public.review SET useful = useful - 1 "
            + "WHERE id = :id";

    private static final String SQL_INSERT_REVIEW_RATE_USER = "INSERT INTO public.review_rate_user "
            + "(review_id, rated_user_id, is_positive) VALUES (:id, :user_id, :is_positive)";

    private static final String SQL_IS_RATED_BY_USER = "SELECT CASE WHEN COUNT(review_id) > 0 THEN TRUE ELSE FALSE END "
            + "FROM public.review_rate_user "
            + "WHERE review_id = :id AND rated_user_id = :user_id AND is_positive = :is_positive";

    private static final String SQL_DELETE_REVIEW_RATE_USER = "DELETE FROM public.review_rate_user "
            + "WHERE review_id = :id AND rated_user_id = :user_id AND is_positive = :is_positive";


    @Override
    public Review insertReview(Review review) {
        MapSqlParameterSource params = getParams(review);
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(SQL_INSERT_REVIEW, params, holder);
        Integer reviewId = holder.getKey().intValue();

        return getReviewById(reviewId).orElseThrow(() -> new ReviewNotFoundException(String.valueOf(reviewId)));
    }

    @Override
    public Review updateReview(Review review) {
        KeyHolder holder = new GeneratedKeyHolder();

        if (getReviewById(review.getReviewId()).isPresent()) {
            MapSqlParameterSource params = getParams(review);

            params.addValue("id", review.getReviewId());
            jdbcTemplate.update(SQL_UPDATE_REVIEW, params, holder);
        }
        Integer filmId = holder.getKey().intValue();

        return getReviewById(filmId).orElseThrow(() -> new ReviewNotFoundException(String.valueOf(filmId)));
    }

    @Override
    public Optional<Review> getReviewById(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(SQL_GET_REVIEW_BY_ID, params, reviewMapper).stream().findFirst();
    }

    @Override
    public void deleteReviewById(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(SQL_DELETE_REVIEW_BY_ID, params);
    }

    @Override
    public List<Review> getAllReviews() {
        return jdbcTemplate.query(SQL_GET_ALL_REVIEWS, reviewMapper);
    }

    @Override
    public List<Review> getReviewsByFilmId(Integer id, Integer count) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        params.addValue("count", count);
        return jdbcTemplate.query(SQL_GET_REVIEWS_BY_FILM_ID, params, reviewMapper);
    }

    @Override
    public Review increaseUsefulRate(Integer id, Integer userId) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(SQL_LIKE_REVIEW, params);
        params.addValue("user_id", userId);
        params.addValue("is_positive", true);
        jdbcTemplate.update(SQL_INSERT_REVIEW_RATE_USER, params);

        return getReviewById(id).orElseThrow(() -> new ReviewNotFoundException(String.valueOf(id)));
    }

    @Override
    public Review decreaseUsefulRate(Integer id, Integer userId) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(SQL_DISLIKE_REVIEW, params);
        params.addValue("user_id", userId);
        params.addValue("is_positive", false);
        jdbcTemplate.update(SQL_INSERT_REVIEW_RATE_USER, params);

        return getReviewById(id).orElseThrow(() -> new ReviewNotFoundException(String.valueOf(id)));
    }

    @Override
    public Boolean isRatedByUser(Integer id, Integer userId, boolean isPositive) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        params.addValue("user_id", userId);
        params.addValue("is_positive", isPositive);
        return jdbcTemplate.queryForObject(SQL_IS_RATED_BY_USER, params, Boolean.class);
    }

    @Override
    public Review deleteUsefulRate(Integer id, Integer userId, boolean isPositive) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        params.addValue("user_id", userId);
        params.addValue("is_positive", isPositive);
        jdbcTemplate.update(SQL_DELETE_REVIEW_RATE_USER, params);
        if (isPositive) {
            jdbcTemplate.update(SQL_DISLIKE_REVIEW, params);
        } else {
            jdbcTemplate.update(SQL_LIKE_REVIEW, params);
        }
        return getReviewById(id).orElseThrow(() -> new ReviewNotFoundException(String.valueOf(id)));
    }

    @Override
    public boolean doesExist(Integer id) {
        return getAllReviews().stream().anyMatch(review -> Objects.equals(review.getReviewId(), id));
    }

    private MapSqlParameterSource getParams(Review review) {
        var params = new MapSqlParameterSource();

        params.addValue("content", review.getContent());
        params.addValue("is_positive", review.getIsPositive());
        params.addValue("user_id", review.getUserId());
        params.addValue("film_id", review.getFilmId());
        params.addValue("useful", review.getUseful());
        return params;
    }
}
