package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.LikedPersonMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final GenreRepository genreRepository;

    private final RatingRepository ratingRepository;

    private final FilmMapper filmMapper;

    private final LikedPersonMapper likedPersonMapper;

    private static final String SQL_INSERT_FILM = "INSERT INTO public.film "
            + "(name, description, release_date, duration, rating_id) VALUES(:name, :description, :release_date, "
            + ":duration, :rating_id)";

    private static final String SQL_UPDATE_FILM = "UPDATE public.film SET name = :name, description = :description, "
            + "release_date = :release_date, duration = :duration, rating_id = :rating_id where id = :id";

    private static final String SQL_GET_FILM_BY_ID = "SELECT id, name, description, release_date, duration, rating_id "
            + "FROM public.film WHERE id = :id";

    private static final String SQL_GET_ALL_FILMS = "SELECT id, name, description, release_date, duration, rating_id "
            + "FROM public.film";

    private static final String SQL_INSERT_LIKE = "INSERT INTO public.film_like (film_id, liked_person_id) "
            + "VALUES (:film_id, :person_id)";

    private static final String SQL_GET_LIKES_BY_FILM_ID = "SELECT liked_person_id FROM public.film_like "
            + "WHERE film_id = :film_id";

    private static final String SQL_DELETE_LIKE = "DELETE FROM public.film_like "
            + "WHERE film_id = :film_id AND liked_person_id = :person_id";

    private static final String SQL_COMMON_FILMS = "SELECT * FROM public.film AS f " +
            "WHERE f.id IN (SELECT film_id " +
            "FROM public.film_like " +
            "WHERE liked_person_id = :userId " +
            "INTERSECT " +
            "SELECT film_id " +
            "FROM public.film_like " +
            "WHERE liked_person_id = :friendId) " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(f.id) DESC ";

    private static final String SQL_DELETE_FILM_BY_ID = "DELETE FROM public.film WHERE id = :id";

    @Override
    public Film insertFilm(Film film) {
        log.info("Creating film with id = {}", film.getId());
        MapSqlParameterSource params = getParams(film);
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(SQL_INSERT_FILM, params, holder);
        Integer filmId = holder.getKey().intValue();

        genreRepository.insertFilmGenres(film.getGenres(), filmId);
        return getFilmById(filmId).orElseThrow(() -> new FilmNotFoundException(String.valueOf(filmId)));
    }

    @Override
    public Film updateFilm(Film film) {
        KeyHolder holder = new GeneratedKeyHolder();

        if (getFilmById(film.getId()).isPresent()) {
            MapSqlParameterSource params = getParams(film);

            params.addValue("id", film.getId());
            jdbcTemplate.update(SQL_UPDATE_FILM, params, holder);
            if (film.getGenres().isEmpty()) {
                genreRepository.deleteFilmGenres(film.getId());
            } else {
                genreRepository.updateFilmGenres(film.getGenres(), film.getId());
            }
        }
        Integer filmId = holder.getKey().intValue();

        return getFilmById(filmId).orElseThrow(() -> new FilmNotFoundException(String.valueOf(filmId)));
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = jdbcTemplate.query(SQL_GET_ALL_FILMS, filmMapper);

        return films.stream().peek(film -> {
            film.setUsersLiked(getUsersLikedByFilmId(film.getId()));
            film.setGenres(genreRepository.getByFilmId(film.getId()));
            film.setMpa(ratingRepository.getByFilmId(film.getId()).orElse(null));
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        Film film = null;
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        Optional<Film> filmOptional = jdbcTemplate.query(SQL_GET_FILM_BY_ID, params, filmMapper).stream().findFirst();

        if (filmOptional.isPresent()) {
            film = filmOptional.get();
            film.setUsersLiked(getUsersLikedByFilmId(id));
            film.setGenres(genreRepository.getByFilmId(id));
            film.setMpa(ratingRepository.getByFilmId(id).orElse(null));
        }
        return Optional.ofNullable(film);
    }

    @Override
    public Film putLike(Integer id, Integer userId) {
        MapSqlParameterSource params = getLikeParams(id, userId);

        jdbcTemplate.update(SQL_INSERT_LIKE, params);
        Film film = getFilmById(id).orElseThrow(() -> new FilmNotFoundException(String.valueOf(id)));
        Set<Integer> usersLiked = getUsersLikedByFilmId(id);

        film.setUsersLiked(usersLiked);
        log.debug("FilmId = {} list of users liked: {}", id, usersLiked);
        return updateFilm(film);
    }

    @Override
    public Film deleteLike(Integer id, Integer userId) {
        MapSqlParameterSource params = getLikeParams(id, userId);

        jdbcTemplate.update(SQL_DELETE_LIKE, params);
        Film film = getFilmById(id).orElseThrow(() -> new FilmNotFoundException(String.valueOf(id)));
        Set<Integer> usersLiked = getUsersLikedByFilmId(id);

        film.setUsersLiked(usersLiked);
        log.debug("FilmId = {} list of users liked: {}", id, usersLiked);
        return updateFilm(film);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return getAllFilms().stream()
                .sorted((film1, film2) -> film2.getUsersLiked().size() - film1.getUsersLiked().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public boolean doesExist(Integer id) {
        return getAllFilms().stream().anyMatch(film -> Objects.equals(film.getId(), id));
    }

    @Override
    public void deleteFilmById(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(SQL_DELETE_FILM_BY_ID, params);
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        var params = new MapSqlParameterSource();

        params.addValue("userId", userId);
        params.addValue("friendId", friendId);
        List<Film> films = jdbcTemplate.query(SQL_COMMON_FILMS, params, filmMapper);
        return films.stream().peek(film -> {
            film.setUsersLiked(getUsersLikedByFilmId(film.getId()));
            film.setGenres(genreRepository.getByFilmId(film.getId()));
            film.setMpa(ratingRepository.getByFilmId(film.getId()).orElse(null));
        }).collect(Collectors.toList());
    }

    private MapSqlParameterSource getParams(Film film) {
        var params = new MapSqlParameterSource();

        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        if (film.getMpa() != null) {
            params.addValue("rating_id", film.getMpa().getId());
        } else {
            params.addValue("rating_id", null);
        }
        return params;
    }

    private MapSqlParameterSource getLikeParams(Integer id, Integer userId) {
        var params = new MapSqlParameterSource();

        params.addValue("film_id", id);
        params.addValue("person_id", userId);
        return params;
    }

    private Set<Integer> getUsersLikedByFilmId(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("film_id", id);
        return new HashSet<>(jdbcTemplate.query(SQL_GET_LIKES_BY_FILM_ID, params, likedPersonMapper));
    }
}
