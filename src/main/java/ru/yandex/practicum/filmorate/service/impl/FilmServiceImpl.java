package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final FilmValidator filmValidator;

    @Override
    public Film createFilm(Film film) {
        filmValidator.validate(film);
        if (film.getUsersLiked() == null) {
            film.setUsersLiked(new HashSet<>());
        }
        return filmRepository.insertFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidator.validate(film);
        if (film.getUsersLiked() == null) {
            film.setUsersLiked(new HashSet<>());
        }
        if (filmRepository.getAllFilms().stream().anyMatch(current -> Objects.equals(current.getId(), film.getId()))) {
            log.info("Updating film with id = {}", film.getId());
            return filmRepository.updateFilm(film);
        }
        throw new FilmNotFoundException(String.valueOf(film.getId()));
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Returning all films");
        return filmRepository.getAllFilms();
    }

    @Override
    public Film getFilmById(Integer id) {
        Optional<Film> userOptional = filmRepository.getFilmById(id);

        if (userOptional.isEmpty()) {
            throw new FilmNotFoundException(String.valueOf(id));
        }
        return userOptional.get();
    }

    @Override
    public Film putLike(Integer id, Integer userId) {
        if (filmRepository.getFilmById(id).isEmpty()) {
            throw new FilmNotFoundException(String.valueOf(id));
        }
        if (userRepository.getUserById(userId).isEmpty()) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        log.info("Adding like from userId = {} to filmId = {}", userId, id);
        return filmRepository.putLike(id, userId);
    }

    @Override
    public Film deleteLike(Integer id, Integer userId) {
        if (filmRepository.getFilmById(id).isEmpty()) {
            throw new FilmNotFoundException(String.valueOf(id));
        }
        if (userRepository.getUserById(userId).isEmpty()) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        log.info("Removing like from userId = {} to filmId = {}", userId, id);
        return filmRepository.deleteLike(id, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        log.info("Getting popular films amount = {}", count);
        return filmRepository.getPopularFilms(count);
    }
}
