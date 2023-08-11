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

    @Override
    public Optional<Rating> getByFilmId(Integer id) {
        String sqlGetRatingByFilmId = "SELECT id, name FROM public.rating "
                + "WHERE id = (SELECT rating_id FROM public.film where id = :id)";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(sqlGetRatingByFilmId, params, ratingMapper).stream().findFirst();
    }

    @Override
    public List<Rating> getAllRatings() {
        String sqlGetAllRatings = "SELECT id, name FROM public.rating";

        return jdbcTemplate.query(sqlGetAllRatings, ratingMapper);
    }

    @Override
    public Optional<Rating> getRatingById(Integer id) {
        String sqlGetRatingById = "SELECT id, name FROM public.rating WHERE id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(sqlGetRatingById, params, ratingMapper).stream().findFirst();
    }
}
