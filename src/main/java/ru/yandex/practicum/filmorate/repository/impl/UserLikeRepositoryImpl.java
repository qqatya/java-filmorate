package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserLikeMapper;
import ru.yandex.practicum.filmorate.repository.UserLikeRepository;

import java.util.HashSet;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserLikeRepositoryImpl implements UserLikeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final UserLikeMapper userLikeMapper;

    @Override
    public Set<Integer> getUsersLikedByFilmId(Integer id) {
        String sqlGetLikesByFilmId = "SELECT liked_person_id FROM public.film_like "
                + "WHERE film_id = :film_id";
        var params = new MapSqlParameterSource();

        params.addValue("film_id", id);
        return new HashSet<>(jdbcTemplate.query(sqlGetLikesByFilmId, params, userLikeMapper));
    }
}
