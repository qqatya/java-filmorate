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

    @Override
    public Set<Genre> getByFilmId(Integer id) {
        String sqlGetGenreByFilmId = "SELECT id, name FROM public.genre WHERE id IN "
                + "(SELECT genre_id FROM film_genre WHERE film_id = :id)";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return new HashSet<>(jdbcTemplate.query(sqlGetGenreByFilmId, params, genreMapper));
    }

    @Override
    public Set<Genre> insertFilmGenres(Set<Genre> genres, Integer filmId) {
        String sqlInsertFilmGenre = "INSERT INTO public.film_genre (film_id, genre_id) VALUES (:id, :genre_id)";
        List<Integer> genreIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
        var params = new MapSqlParameterSource();

        params.addValue("id", filmId);
        for (Integer genreId : genreIds) {
            params.addValue("genre_id", genreId);
            jdbcTemplate.update(sqlInsertFilmGenre, params);
        }
        return getByFilmId(filmId);
    }

    @Override
    public Set<Genre> updateFilmGenres(Set<Genre> genres, Integer filmId) {
        String sqlInsertFilmGenre = "INSERT INTO public.film_genre (film_id, genre_id) VALUES (:id, :genre_id)";
        String sqlUpdateFilmGenre = "UPDATE public.film_genre SET genre_id = :genre_id WHERE film_id = :id "
                + "AND genre_id = :genre_id";
        String sqlDeleteFilmGenreById = "DELETE FROM public.film_genre WHERE film_id = :id AND genre_id = :genre_id";
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
                jdbcTemplate.update(sqlUpdateFilmGenre, params);
            } else {
                jdbcTemplate.update(sqlInsertFilmGenre, params);
            }
        }
        existingGenresIds.removeAll(genreIds);
        for (Integer id : existingGenresIds) {
            params.addValue("genre_id", id);
            jdbcTemplate.update(sqlDeleteFilmGenreById, params);
        }
        return getByFilmId(filmId);
    }

    @Override
    public void deleteFilmGenres(Integer filmId) {
        String sqlDeleteFilmGenres = "DELETE FROM public.film_genre WHERE film_id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", filmId);
        jdbcTemplate.update(sqlDeleteFilmGenres, params);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlGetAllGenres = "SELECT id, name FROM public.genre";

        return jdbcTemplate.query(sqlGetAllGenres, genreMapper);
    }

    @Override
    public Optional<Genre> getGenreById(Integer id) {
        String sqlGetGenreById = "SELECT id, name FROM public.genre WHERE id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(sqlGetGenreById, params, genreMapper).stream().findFirst();
    }

}
