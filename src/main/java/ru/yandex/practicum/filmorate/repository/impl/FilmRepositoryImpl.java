package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.type.SearchType;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.UserLikeRepository;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.type.ExceptionType.FILM_NOT_FOUND;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final GenreRepository genreRepository;

    private final DirectorRepository directorRepository;

    private final FilmMapper filmMapper;

    private final UserLikeRepository userLikeRepository;

    @Override
    public Film insertFilm(Film film) {
        String sqlInsertFilm = "INSERT INTO public.film (name, description, release_date, duration, rating_id) "
                + "VALUES(:name, :description, :release_date, :duration, :rating_id)";
        log.info("Creating film with id = {}", film.getId());
        MapSqlParameterSource params = getParams(film);
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(sqlInsertFilm, params, holder);
        Integer filmId = holder.getKey().intValue();

        genreRepository.insertFilmGenres(film.getGenres(), filmId);
        directorRepository.insertFilmDirectors(film.getDirectors(), filmId);
        return getFilmById(filmId).orElseThrow(() -> new NotFoundException(FILM_NOT_FOUND.getValue() + filmId));
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlUpdateFilm = "UPDATE public.film SET name = :name, description = :description, "
                + "release_date = :release_date, duration = :duration, rating_id = :rating_id where id = :id";
        KeyHolder holder = new GeneratedKeyHolder();

        if (getFilmById(film.getId()).isPresent()) {
            MapSqlParameterSource params = getParams(film);

            params.addValue("id", film.getId());
            jdbcTemplate.update(sqlUpdateFilm, params, holder);
            if (film.getGenres().isEmpty()) {
                genreRepository.deleteFilmGenres(film.getId());
            } else {
                genreRepository.updateFilmGenres(film.getGenres(), film.getId());
            }
            if (film.getDirectors().isEmpty()) {
                directorRepository.deleteFilmDirectors(film.getId());
            } else {
                directorRepository.updateFilmDirectors(film.getDirectors(), film.getId());
            }
        }
        Integer filmId = holder.getKey().intValue();

        return getFilmById(filmId).orElseThrow(() -> new NotFoundException(FILM_NOT_FOUND.getValue() + filmId));
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlGetAllFilms = "SELECT id, name, description, release_date, duration, rating_id "
                + "FROM public.film";

        return jdbcTemplate.query(sqlGetAllFilms, filmMapper);
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        String sqlGetFilmById = "SELECT id, name, description, release_date, duration, rating_id "
                + "FROM public.film WHERE id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(sqlGetFilmById, params, filmMapper).stream().findFirst();
    }

    @Override
    public Film putLike(Integer id, Integer userId, Double grade) {
        String sqlInsertLike = "INSERT INTO public.film_like (film_id, liked_person_id, grade) "
                + "VALUES (:film_id, :person_id, :grade)";
        MapSqlParameterSource params = getLikeParams(id, userId);

        params.addValue("grade", grade);
        jdbcTemplate.update(sqlInsertLike, params);
        Film film = getFilmById(id).orElseThrow(() -> new NotFoundException(FILM_NOT_FOUND.getValue() + id));
        Set<Integer> usersLiked = userLikeRepository.getUsersLikedByFilmId(id);

        film.setUsersLiked(usersLiked);
        log.debug("FilmId = {} list of users liked: {}", id, usersLiked);
        return updateFilm(film);
    }

    @Override
    public Film deleteLike(Integer id, Integer userId) {
        String sqlDeleteLike = "DELETE FROM public.film_like WHERE film_id = :film_id AND liked_person_id = :person_id";
        MapSqlParameterSource params = getLikeParams(id, userId);

        jdbcTemplate.update(sqlDeleteLike, params);
        Film film = getFilmById(id).orElseThrow(() -> new NotFoundException(FILM_NOT_FOUND.getValue() + id));
        Set<Integer> usersLiked = userLikeRepository.getUsersLikedByFilmId(id);

        film.setUsersLiked(usersLiked);
        log.debug("FilmId = {} list of users liked: {}", id, usersLiked);
        return updateFilm(film);
    }

    @Override
    public List<Film> getPopularFilms(Integer count, Integer genreId, Integer year) {
        String sqlPopularFilms = "SELECT f.id, f.name, f.description,f.release_date, f.duration, f.rating_id "
                + "FROM film AS f LEFT JOIN film_like AS fl ON fl.film_id = f.id GROUP BY f.id "
                + "ORDER BY AVG(fl.grade) DESC LIMIT :count ";
        String sqlPopularFilmsGenre = "SELECT * FROM public.film AS f LEFT JOIN film_like AS fl ON f.id = fl.film_id "
                + "LEFT JOIN film_genre AS fg ON f.id = fg.film_id WHERE fg.genre_id = :genreId GROUP BY f.id "
                + "ORDER BY AVG(fl.grade) DESC LIMIT :count ";
        String sqlPopularFilmsYear = "SELECT f.id, f.name, f.description,f.release_date, f.duration, f.rating_id "
                + "FROM public.film AS f LEFT JOIN film_like AS fl ON f.id = fl.film_id "
                + "WHERE EXTRACT(YEAR FROM f.release_date) = :year GROUP BY f.id ORDER BY AVG(fl.grade) DESC "
                + "LIMIT :count ";
        String sqlPopularFilmsGenreYear = "SELECT f.id, f.name, f.description,f.release_date, f.duration, f.rating_id "
                + "FROM public.film AS f LEFT JOIN film_like AS fl ON f.id = fl.film_id LEFT JOIN film_genre AS fg "
                + "ON f.id = fg.film_id WHERE fg.genre_id = :genreId AND EXTRACT(YEAR FROM f.release_date) = :year "
                + "GROUP BY f.id ORDER BY AVG(fl.grade) DESC LIMIT :count ";
        var params = new MapSqlParameterSource();
        params.addValue("count", count);
        if (genreId != null && year == null) {
            params.addValue("genreId", genreId);
            return jdbcTemplate.query(sqlPopularFilmsGenre, params, filmMapper);
        }
        if (year != null && genreId == null) {
            params.addValue("year", year);
            return jdbcTemplate.query(sqlPopularFilmsYear, params, filmMapper);
        }
        if (genreId != null && year != null) {
            params.addValue("genreId", genreId);
            params.addValue("year", year);
            return jdbcTemplate.query(sqlPopularFilmsGenreYear, params, filmMapper);
        }
        return jdbcTemplate.query(sqlPopularFilms, params, filmMapper);
    }

    @Override
    public boolean doesExist(Integer id) {
        return getAllFilms().stream().anyMatch(film -> Objects.equals(film.getId(), id));
    }

    @Override
    public void deleteFilmById(Integer id) {
        String sqlDeleteFilmById = "DELETE FROM public.film WHERE id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(sqlDeleteFilmById, params);
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        String sqlCommonFilms = "SELECT * FROM public.film AS f WHERE f.id IN (SELECT film_id FROM public.film_like "
                + "WHERE liked_person_id = :userId INTERSECT SELECT film_id FROM public.film_like "
                + "WHERE liked_person_id = :friendId) GROUP BY f.id ORDER BY COUNT(f.id) DESC ";
        var params = new MapSqlParameterSource();

        params.addValue("userId", userId);
        params.addValue("friendId", friendId);
        return jdbcTemplate.query(sqlCommonFilms, params, filmMapper);
    }

    @Override
    public List<Film> searchFilms(String query, String by) {
        String sqlPopularFilms = "SELECT f.id, f.name, f.description,f.release_date, f.duration, f.rating_id "
                + "FROM film AS f LEFT JOIN film_like AS fl ON fl.film_id = f.id GROUP BY f.id "
                + "ORDER BY AVG(fl.grade) DESC LIMIT :count ";
        String sqlGetFilmsSearchInTitle = "SELECT film.id, film.name, film.description, film.release_date, "
                + "film.duration, film.rating_id FROM public.film LEFT JOIN public.film_like AS fl "
                + "ON film.id = fl.film_id WHERE LOWER(film.name) LIKE CONCAT('%', :query, '%') GROUP BY film.id "
                + "ORDER BY AVG(fl.grade) DESC";
        String sqlGetFilmsSearchInDirector = "SELECT film.id, film.name, film.description, film.release_date, "
                + "film.duration, film.rating_id FROM public.film LEFT JOIN public.film_like AS fl "
                + "ON film.id = fl.film_id WHERE film.id IN (SELECT fd.film_id FROM public.director "
                + "LEFT JOIN public.film_director AS fd ON fd.director_id = director.id WHERE LOWER(director.name) "
                + "LIKE CONCAT('%', :query, '%')) GROUP BY film.id ORDER BY AVG(fl.grade) DESC";
        String sqlGetFilmsSearchInDirectorAndTitle = "SELECT film.* FROM film LEFT JOIN film_like AS fl "
                + "ON film.id = fl.film_id WHERE film.id IN (SELECT fd.film_id FROM director LEFT JOIN film_director fd "
                + "ON fd.director_id = director.id WHERE LOWER(director.name) LIKE CONCAT('%', :query, '%')) "
                + "OR LOWER(film.name) LIKE CONCAT('%', :query, '%') GROUP BY film.id "
                + "ORDER BY COUNT(fl.liked_person_id) DESC";
        var params = new MapSqlParameterSource();
        params.addValue("query", query.toLowerCase());
        SearchType type = SearchType.of(by);
        switch (type) {
            case DIRECTOR:
                return jdbcTemplate.query(sqlGetFilmsSearchInDirector, params, filmMapper);
            case TITLE:
                return jdbcTemplate.query(sqlGetFilmsSearchInTitle, params, filmMapper);
            case TITLE_DIRECTOR:
            case DIRECTOR_TITLE:
                return jdbcTemplate.query(sqlGetFilmsSearchInDirectorAndTitle, params, filmMapper);
            default:
                return jdbcTemplate.query(sqlPopularFilms, params, filmMapper);
        }
    }

    @Override
    public List<Film> getFilmsByDirectorId(Integer id, String sortBy) {
        String sqlGetFilmsByDirectorId = "SELECT film.id, film.name, film.description, film.release_date, "
                + "film.duration, film.rating_id FROM public.film INNER JOIN public.film_director "
                + "ON film_director.film_id = film.id "
                + "WHERE film_director.director_id = :director_id";
        String sqlGetFilmsByGrade = "SELECT f.id, f.name, f.description,f.release_date, f.duration, f.rating_id "
                + "FROM public.film AS f LEFT JOIN public.film_like AS l ON f.id = l.film_id "
                + "WHERE f.id IN (SELECT d.film_id FROM public.film_director AS d WHERE d.director_id = :director_id )"
                + "GROUP BY f.id ORDER BY AVG(l.grade) DESC";
        var params = new MapSqlParameterSource();
        params.addValue("director_id", id);
        List<Film> films = new ArrayList<>(jdbcTemplate.query(sqlGetFilmsByDirectorId, params, filmMapper));

        if ("year".equals(sortBy)) {
            return films.stream()
                    .sorted(Comparator.comparingInt(film -> film.getReleaseDate().getYear()))
                    .collect(Collectors.toList());
        } else {
            return jdbcTemplate.query(sqlGetFilmsByGrade, params, filmMapper);
        }
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
}