package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.type.ExceptionType.DIRECTOR_NOT_FOUND;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DirectorRepositoryImpl implements DirectorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final DirectorMapper directorMapper;

    private MapSqlParameterSource getParams(Director director) {
        var params = new MapSqlParameterSource();
        params.addValue("name", director.getName());
        return params;
    }

    @Override
    public Director insertDirector(Director director) {
        String sqlInsertDirector = "INSERT INTO public.director (name) VALUES(:name)";

        log.info("Creating director with id = {}", director.getId());
        MapSqlParameterSource params = getParams(director);
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(sqlInsertDirector, params, holder);
        Integer directorId = holder.getKey().intValue();

        return getDirectorById(directorId)
                .orElseThrow(() -> new NotFoundException(DIRECTOR_NOT_FOUND.getValue() + directorId));
    }

    @Override
    public Director updateDirector(Director director) {
        String sqlUpdateDirector = "UPDATE public.director SET name = :name where id = :id";
        KeyHolder holder = new GeneratedKeyHolder();

        if (getDirectorById(director.getId()).isPresent()) {
            MapSqlParameterSource params = getParams(director);

            params.addValue("id", director.getId());
            jdbcTemplate.update(sqlUpdateDirector, params, holder);
        }
        Integer directorId = holder.getKey().intValue();
        return getDirectorById(directorId)
                .orElseThrow(() -> new NotFoundException(DIRECTOR_NOT_FOUND.getValue() + directorId));
    }

    @Override
    public List<Director> getAllDirectors() {
        String sqlGetAllDirectors = "SELECT id, name FROM public.director";

        return jdbcTemplate.query(sqlGetAllDirectors, directorMapper);
    }

    @Override
    public Optional<Director> getDirectorById(Integer id) {
        String sqlGetDirectorById = "SELECT id, name FROM public.director WHERE id = :id";

        Director director = null;
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        Optional<Director> directorOptional = jdbcTemplate.query(sqlGetDirectorById, params, directorMapper)
                .stream().findFirst();

        if (directorOptional.isPresent()) {
            director = directorOptional.get();
        }
        return Optional.ofNullable(director);
    }

    @Override
    public Set<Director> getByFilmId(Integer id) {
        String sqlGetDirectorByFilmId = "SELECT id, name FROM public.director WHERE id in "
                + "(SELECT director_id from film_director WHERE film_id = :id)";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return new HashSet<>(jdbcTemplate.query(sqlGetDirectorByFilmId, params, directorMapper));
    }

    @Override
    public void deleteDirectorById(Integer id) {
        String sqlDeleteDirectorById = "DELETE FROM public.director WHERE id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(sqlDeleteDirectorById, params);
    }

    @Override
    public boolean doesExist(Integer id) {
        return getAllDirectors().stream().anyMatch(director -> Objects.equals(director.getId(), id));
    }

    @Override
    public Set<Director> insertFilmDirectors(Set<Director> directors, Integer filmId) {
        String sqlInsertFilmDirector = "INSERT INTO public.film_director (film_id, director_id) "
                + "VALUES (:id, :director_id)";
        String sqlGetDirectorByFilmId = "SELECT id, name FROM public.director WHERE id in "
                + "(SELECT director_id from film_director WHERE film_id = :id)";
        List<Integer> directorIds = directors.stream()
                .map(Director::getId)
                .collect(Collectors.toList());
        var params = new MapSqlParameterSource();

        params.addValue("id", filmId);
        for (Integer directorId : directorIds) {
            params.addValue("director_id", directorId);
            jdbcTemplate.update(sqlInsertFilmDirector, params);
        }
        return new HashSet<>(jdbcTemplate.query(sqlGetDirectorByFilmId, params, directorMapper));
    }

    @Override
    public Set<Director> updateFilmDirectors(Set<Director> directors, Integer filmId) {
        String sqlInsertFilmDirector = "INSERT INTO public.film_director (film_id, director_id) "
                + "VALUES (:id, :director_id)";
        String sqlGetDirectorByFilmId = "SELECT id, name FROM public.director WHERE id in "
                + "(SELECT director_id from film_director WHERE film_id = :id)";
        String sqlUpdateFilmDirector = "UPDATE public.film_director SET director_id = :director_id "
                + "WHERE film_id = :id AND director_id = :director_id";
        String sqlDeleteFilmDirectorById = "DELETE FROM public.film_DIRECTOR WHERE film_id = :id "
                + "AND director_id = :director_id";
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
                jdbcTemplate.update(sqlUpdateFilmDirector, params);
            } else {
                jdbcTemplate.update(sqlInsertFilmDirector, params);
            }
        }
        existingDirectorsIds.removeAll(directorIds);
        for (Integer id : existingDirectorsIds) {
            params.addValue("director_id", id);
            jdbcTemplate.update(sqlDeleteFilmDirectorById, params);
        }
        return new HashSet<>(jdbcTemplate.query(sqlGetDirectorByFilmId, params, directorMapper));
    }

    @Override
    public void deleteFilmDirectors(Integer filmId) {
        String sqlDeleteFilmDirectors = "DELETE FROM public.film_director WHERE film_id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", filmId);
        jdbcTemplate.update(sqlDeleteFilmDirectors, params);
    }
}