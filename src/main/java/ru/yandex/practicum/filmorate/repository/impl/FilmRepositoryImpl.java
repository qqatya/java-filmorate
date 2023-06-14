package ru.yandex.practicum.filmorate.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class FilmRepositoryImpl implements FilmRepository {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idCounter = 0;

    @Override
    public Film insertFilm(Film film) {
        film.setId(++idCounter);
        log.info("Creating film with id = {}", film.getId());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        return films.values().stream()
                .filter(film -> Objects.equals(film.getId(), id))
                .findFirst();
    }

    @Override
    public Film putLike(Integer id, Integer userId) {
        Film film = films.get(id);
        Set<Integer> usersLiked = film.getUsersLiked();

        usersLiked.add(userId);
        film.setUsersLiked(usersLiked);
        films.put(film.getId(), film);
        log.debug("FilmId = {} list of users liked: {}", id, usersLiked);
        return films.get(id);
    }

    @Override
    public Film deleteLike(Integer id, Integer userId) {
        Film film = films.get(id);
        Set<Integer> usersLiked = film.getUsersLiked();

        usersLiked.remove(userId);
        film.setUsersLiked(usersLiked);
        films.put(film.getId(), film);
        log.debug("FilmId = {} list of users liked: {}", id, usersLiked);
        return films.get(id);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return films.values().stream()
                .sorted((film1, film2) -> film2.getUsersLiked().size() - film1.getUsersLiked().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
