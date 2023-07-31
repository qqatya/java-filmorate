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
            + "is_positive = :is_positive, useful = :useful "
            + "WHERE id = :id";

    private static final String SQL_GET_REVIEW_BY_ID = "SELECT id, content, is_positive, user_id, film_id, useful "
            + "FROM public.review WHERE id = :id";

    private static final String SQL_DELETE_REVIEW_BY_ID = "DELETE FROM public.review WHERE id = :id";

    private static final String SQL_GET_ALL_REVIEWS = "SELECT id, content, is_positive, user_id, film_id, useful "
            + "FROM public.review";


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
