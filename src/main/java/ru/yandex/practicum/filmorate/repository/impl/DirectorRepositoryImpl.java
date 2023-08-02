package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DirectorRepositoryImpl implements DirectorRepository {
    private static final String SQL_INSERT_DIRECTOR = "INSERT INTO public.director (name) VALUES(:name)";
    private static final String SQL_UPDATE_DIRECTOR = "UPDATE public.director SET name = :name where id = :id";
    private static final String SQL_GET_DIRECTOR_BY_ID = "SELECT id, name FROM public.director WHERE id = :id";
    private static final String SQL_GET_ALL_DIRECTORS = "SELECT id, name FROM public.director";
    private static final String SQL_DELETE_DIRECTOR_BY_ID = "DELETE FROM public.director WHERE id = :id";
    private static final String SQL_INSERT_FILM_DIRECTOR = "INSERT INTO public.film_director (film_id, director_id) "
            + "VALUES (:id, :director_id)";
    private static final String SQL_GET_DIRECTOR_BY_FILM_ID = "SELECT id, name FROM public.director WHERE id in "
            + "(SELECT director_id from film_director WHERE film_id = :id)";
    private static final String SQL_UPDATE_FILM_DIRECTOR = "UPDATE public.film_director SET director_id = :director_id "
            + "WHERE film_id = :id AND director_id = :director_id";

    private static final String SQL_DELETE_FILM_DIRECTORS = "DELETE FROM public.film_director WHERE film_id = :id";

    private static final String SQL_DELETE_FILM_DIRECTOR_BY_ID = "DELETE FROM public.film_DIRECTOR WHERE film_id = :id "
            + "AND director_id = :director_id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DirectorMapper directorMapper;

    private MapSqlParameterSource getParams(Director director) {
        var params = new MapSqlParameterSource();
        params.addValue("name", director.getName());
        return params;
    }

    @Override
    public Director insertDirector(Director director) {
        log.info("Creating director with id = {}", director.getId());
        MapSqlParameterSource params = getParams(director);
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(SQL_INSERT_DIRECTOR, params, holder);
        Integer directorId = holder.getKey().intValue();

        return getDirectorById(directorId).orElseThrow(() -> new DirectorNotFoundException(String.valueOf(directorId)));
    }

    @Override
    public Director updateDirector(Director director) {
        KeyHolder holder = new GeneratedKeyHolder();

        if (getDirectorById(director.getId()).isPresent()) {
            MapSqlParameterSource params = getParams(director);

            params.addValue("id", director.getId());
            jdbcTemplate.update(SQL_UPDATE_DIRECTOR, params, holder);
        }
        Integer directorId = holder.getKey().intValue();
        return getDirectorById(directorId).orElseThrow(() -> new DirectorNotFoundException(String.valueOf(directorId)));
    }

    @Override
    public List<Director> getAllDirectors() {
        return jdbcTemplate.query(SQL_GET_ALL_DIRECTORS, directorMapper);
    }

    @Override
    public Optional<Director> getDirectorById(Integer id) {
        Director director = null;
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        Optional<Director> directorOptional = jdbcTemplate.query(SQL_GET_DIRECTOR_BY_ID, params, directorMapper)
                .stream().findFirst();

        if (directorOptional.isPresent()) {
            director = directorOptional.get();
        }
        return Optional.ofNullable(director);
    }

    @Override
    public Set<Director> getByFilmId(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return new HashSet<>(jdbcTemplate.query(SQL_GET_DIRECTOR_BY_FILM_ID, params, directorMapper));
    }

    @Override
    public void deleteDirectorById(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(SQL_DELETE_DIRECTOR_BY_ID, params);
    }

    @Override
    public boolean doesExist(Integer id) {
        return getAllDirectors().stream().anyMatch(director -> Objects.equals(director.getId(), id));
    }

    @Override
    public Set<Director> insertFilmDirectors(Set<Director> directors, Integer filmId) {
        List<Integer> directorIds = directors.stream()
                .map(Director::getId)
                .collect(Collectors.toList());
        var params = new MapSqlParameterSource();

        params.addValue("id", filmId);
        for (Integer directorId : directorIds) {
            params.addValue("director_id", directorId);
            jdbcTemplate.update(SQL_INSERT_FILM_DIRECTOR, params);
        }
        return new HashSet<>(jdbcTemplate.query(SQL_GET_DIRECTOR_BY_FILM_ID, params, directorMapper));
    }

    @Override
    public Set<Director> updateFilmDirectors(Set<Director> directors, Integer filmId) {
        Set<Integer> existingDirectorsIds = getByFilmId(filmId).stream()
                .map(Director::getId)
                .collect(Collectors.toSet());
        Set<Integer> directorIds = directors.stream()
                .map(Director::getId)
                .collect(Collectors.toSet());
        var params = new MapSqlParameterSource();

        params.addValue("id", filmId);
        for (Integer directorId : directorIds) {
            params.addValue("director_id", directorId);
            if (existingDirectorsIds.stream().anyMatch(existingId -> existingId.equals(directorId))) {
                jdbcTemplate.update(SQL_UPDATE_FILM_DIRECTOR, params);
            } else {
                jdbcTemplate.update(SQL_INSERT_FILM_DIRECTOR, params);
            }
        }
        existingDirectorsIds.removeAll(directorIds);
        for (Integer id : existingDirectorsIds) {
            params.addValue("director_id", id);
            jdbcTemplate.update(SQL_DELETE_FILM_DIRECTOR_BY_ID, params);
        }
        return new HashSet<>(jdbcTemplate.query(SQL_GET_DIRECTOR_BY_FILM_ID, params, directorMapper));
    }

    @Override
    public void deleteFilmDirectors(Integer filmId) {
        var params = new MapSqlParameterSource();

        params.addValue("id", filmId);
        jdbcTemplate.update(SQL_DELETE_FILM_DIRECTORS, params);
    }
}