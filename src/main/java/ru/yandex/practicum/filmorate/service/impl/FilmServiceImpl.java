package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.eventenum.Entity;
import ru.yandex.practicum.filmorate.model.eventenum.Operation;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.impl.EventRepositoryImpl;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final FilmValidator filmValidator;
    private final EventRepositoryImpl eventRepositoryImpl;

    @Override
    public Film createFilm(Film film) {
        filmValidator.validate(film);
        return filmRepository.insertFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidator.validate(film);
        if (filmRepository.doesExist(film.getId())) {
            log.info("Updating filmId = {}", film.getId());
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
        log.info("Getting filmId = {}", id);
        return filmRepository.getFilmById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.valueOf(id)));
    }

    @Override
    public Film putLike(Integer id, Integer userId) {
        if (!filmRepository.doesExist(id)) {
            throw new FilmNotFoundException(String.valueOf(id));
        }
        if (!userRepository.doesExist(userId)) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        log.info("Adding like from userId = {} to filmId = {}", userId, id);
        eventRepositoryImpl.addEvent(new Event(Operation.ADD, Entity.LIKE, id, userId));
        return filmRepository.putLike(id, userId);
    }

    @Override
    public Film deleteLike(Integer id, Integer userId) {
        if (!filmRepository.doesExist(id)) {
            throw new FilmNotFoundException(String.valueOf(id));
        }
        if (!userRepository.doesExist(userId)) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        log.info("Removing like from userId = {} to filmId = {}", userId, id);
        eventRepositoryImpl.addEvent(new Event(Operation.REMOVE, Entity.LIKE, id, userId));
        return filmRepository.deleteLike(id, userId);

    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        log.info("Getting popular films amount = {}", count);
        return filmRepository.getPopularFilms(count);
    }
}
