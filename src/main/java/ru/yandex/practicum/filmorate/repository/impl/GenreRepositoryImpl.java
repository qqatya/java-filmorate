package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryImpl implements GenreRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final GenreMapper genreMapper;

    private static final String SQL_GET_GENRE_BY_FILM_ID = "SELECT id, name FROM public.genre WHERE id in "
            + "(SELECT genre_id from film_genre WHERE film_id = :id)";

    private static final String SQL_INSERT_FILM_GENRE = "INSERT INTO public.film_genre (film_id, genre_id) "
            + "VALUES (:id, :genre_id)";

    private static final String SQL_UPDATE_FILM_GENRE = "UPDATE public.film_genre SET genre_id = :genre_id "
            + "WHERE film_id = :id AND genre_id = :genre_id";

    private static final String SQL_DELETE_FILM_GENRES = "DELETE FROM public.film_genre WHERE film_id = :id";

    private static final String SQL_DELETE_FILM_GENRE_BY_ID = "DELETE FROM public.film_genre WHERE film_id = :id "
            + "AND genre_id = :genre_id";

    private static final String SQL_GET_ALL_GENRES = "SELECT id, name FROM public.genre";

    private static final String SQL_GET_GENRE_BY_ID = "SELECT id, name FROM public.genre WHERE id = :id";

    @Override
    public Set<Genre> getByFilmId(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return new HashSet<>(jdbcTemplate.query(SQL_GET_GENRE_BY_FILM_ID, params, genreMapper));
    }

    @Override
    public Set<Genre> insertFilmGenres(Set<Genre> genres, Integer filmId) {
        List<Integer> genreIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
        var params = new MapSqlParameterSource();

        params.addValue("id", filmId);
        for (Integer genreId : genreIds) {
            params.addValue("genre_id", genreId);
            jdbcTemplate.update(SQL_INSERT_FILM_GENRE, params);
        }
        return new HashSet<>(jdbcTemplate.query(SQL_GET_GENRE_BY_FILM_ID, params, genreMapper));
    }

    @Override
    public Set<Genre> updateFilmGenres(Set<Genre> genres, Integer filmId) {
        Set<Integer> existingGenresIds = getByFilmId(filmId).stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        Set<Integer> genreIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        var params = new MapSqlParameterSource();

        params.addValue("id", filmId);
        for (Integer genreId : genreIds) {
            params.addValue("genre_id", genreId);
            if (existingGenresIds.stream().anyMatch(existingId -> existingId.equals(genreId))) {
                jdbcTemplate.update(SQL_UPDATE_FILM_GENRE, params);
            } else {
                jdbcTemplate.update(SQL_INSERT_FILM_GENRE, params);
            }
        }
        existingGenresIds.removeAll(genreIds);
        for (Integer id : existingGenresIds) {
            params.addValue("genre_id", id);
            jdbcTemplate.update(SQL_DELETE_FILM_GENRE_BY_ID, params);
        }
        return new HashSet<>(jdbcTemplate.query(SQL_GET_GENRE_BY_FILM_ID, params, genreMapper));
    }

    @Override
    public void deleteFilmGenres(Integer filmId) {
        var params = new MapSqlParameterSource();

        params.addValue("id", filmId);
        jdbcTemplate.update(SQL_DELETE_FILM_GENRES, params);
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query(SQL_GET_ALL_GENRES, genreMapper);
    }

    @Override
    public Optional<Genre> getGenreById(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(SQL_GET_GENRE_BY_ID, params, genreMapper).stream().findFirst();
    }

}
