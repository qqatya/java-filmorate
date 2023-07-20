package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RatingRepositoryImpl implements RatingRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RatingMapper ratingMapper;

    private static final String SQL_GET_RATING_BY_FILM_ID = "SELECT id, name FROM public.rating "
            + "WHERE id = (SELECT rating_id FROM public.film where id = :id)";

    private static final String SQL_GET_ALL_RATINGS = "SELECT id, name FROM public.rating";

    private static final String SQL_GET_RATING_BY_ID = "SELECT id, name FROM public.rating WHERE id = :id";


    @Override
    public Optional<Rating> getByFilmId(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(SQL_GET_RATING_BY_FILM_ID, params, ratingMapper).stream().findFirst();
    }

    @Override
    public List<Rating> getAllRatings() {
        return jdbcTemplate.query(SQL_GET_ALL_RATINGS, ratingMapper);
    }

    @Override
    public Optional<Rating> getRatingById(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(SQL_GET_RATING_BY_ID, params, ratingMapper).stream().findFirst();
    }
}
