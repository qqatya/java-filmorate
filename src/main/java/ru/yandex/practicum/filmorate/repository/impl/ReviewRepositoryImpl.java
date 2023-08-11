package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.model.type.ExceptionType.REVIEW_NOT_FOUND;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final ReviewMapper reviewMapper;

    @Override
    public Review insertReview(Review review) {
        String sqlInsertReview = "INSERT INTO public.review "
                + "(content, is_positive, user_id, film_id, useful) VALUES(:content, :is_positive, :user_id, "
                + ":film_id, :useful)";
        MapSqlParameterSource params = getParams(review);
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(sqlInsertReview, params, holder);
        Integer reviewId = holder.getKey().intValue();

        return getReviewById(reviewId).orElseThrow(() -> new NotFoundException(REVIEW_NOT_FOUND.getValue() + reviewId));
    }

    @Override
    public Review updateReview(Review review) {
        String sqlUpdateReview = "UPDATE public.review SET content = :content, is_positive = :is_positive "
                + "WHERE id = :id";
        KeyHolder holder = new GeneratedKeyHolder();

        if (getReviewById(review.getReviewId()).isPresent()) {
            MapSqlParameterSource params = getParams(review);

            params.addValue("id", review.getReviewId());
            jdbcTemplate.update(sqlUpdateReview, params, holder);
        }
        Integer filmId = holder.getKey().intValue();

        return getReviewById(filmId)
                .orElseThrow(() -> new NotFoundException(REVIEW_NOT_FOUND.getValue() + review.getReviewId()));
    }

    @Override
    public Optional<Review> getReviewById(Integer id) {
        String sqlGetReviewById = "SELECT id, content, is_positive, user_id, film_id, useful FROM public.review "
                + "WHERE id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(sqlGetReviewById, params, reviewMapper).stream().findFirst();
    }

    @Override
    public void deleteReviewById(Integer id) {
        String sqlDeleteReviewById = "DELETE FROM public.review WHERE id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(sqlDeleteReviewById, params);
    }

    @Override
    public List<Review> getAllReviews() {
        String sqlGetAllReviews = "SELECT id, content, is_positive, user_id, film_id, useful FROM public.review "
                + "ORDER BY useful DESC";

        return jdbcTemplate.query(sqlGetAllReviews, reviewMapper);
    }

    @Override
    public List<Review> getReviewsByFilmId(Integer id, Integer count) {
        String sqlGetReviewsByFilmId = "SELECT id, content, is_positive, user_id, film_id, useful FROM public.review "
                + "WHERE film_id = :id ORDER BY useful DESC LIMIT :count";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        params.addValue("count", count);
        return jdbcTemplate.query(sqlGetReviewsByFilmId, params, reviewMapper);
    }

    @Override
    public Review increaseUsefulRate(Integer id, Integer userId) {
        String sqlLikeReview = "UPDATE public.review SET useful = useful + 1 WHERE id = :id";
        String sqlInsertReviewRateUser = "INSERT INTO public.review_rate_user (review_id, rated_user_id, is_positive) "
                + "VALUES (:id, :user_id, :is_positive)";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(sqlLikeReview, params);
        params.addValue("user_id", userId);
        params.addValue("is_positive", true);
        jdbcTemplate.update(sqlInsertReviewRateUser, params);

        return getReviewById(id).orElseThrow(() -> new NotFoundException(REVIEW_NOT_FOUND.getValue() + id));
    }

    @Override
    public Review decreaseUsefulRate(Integer id, Integer userId) {
        String sqlDislikeReview = "UPDATE public.review SET useful = useful - 1 WHERE id = :id";
        String sqlInsertReviewRateUser = "INSERT INTO public.review_rate_user (review_id, rated_user_id, is_positive) "
                + "VALUES (:id, :user_id, :is_positive)";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(sqlDislikeReview, params);
        params.addValue("user_id", userId);
        params.addValue("is_positive", false);
        jdbcTemplate.update(sqlInsertReviewRateUser, params);

        return getReviewById(id).orElseThrow(() -> new NotFoundException(REVIEW_NOT_FOUND.getValue() + id));
    }

    @Override
    public Boolean isRatedByUser(Integer id, Integer userId, boolean isPositive) {
        String sqlIsRatedByUser = "SELECT CASE WHEN COUNT(review_id) > 0 THEN TRUE ELSE FALSE END "
                + "FROM public.review_rate_user WHERE review_id = :id AND rated_user_id = :user_id "
                + "AND is_positive = :is_positive";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        params.addValue("user_id", userId);
        params.addValue("is_positive", isPositive);
        return jdbcTemplate.queryForObject(sqlIsRatedByUser, params, Boolean.class);
    }

    @Override
    public Review deleteUsefulRate(Integer id, Integer userId, boolean isPositive) {
        String sqlDeleteReviewRateUser = "DELETE FROM public.review_rate_user WHERE review_id = :id "
                + "AND rated_user_id = :user_id AND is_positive = :is_positive";
        String sqlDislikeReview = "UPDATE public.review SET useful = useful - 1 WHERE id = :id";
        var params = new MapSqlParameterSource();
        String sqlLikeReview = "UPDATE public.review SET useful = useful + 1 WHERE id = :id";

        params.addValue("id", id);
        params.addValue("user_id", userId);
        params.addValue("is_positive", isPositive);
        jdbcTemplate.update(sqlDeleteReviewRateUser, params);
        if (isPositive) {
            jdbcTemplate.update(sqlDislikeReview, params);
        } else {
            jdbcTemplate.update(sqlLikeReview, params);
        }
        return getReviewById(id).orElseThrow(() -> new NotFoundException(REVIEW_NOT_FOUND.getValue() + id));
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
